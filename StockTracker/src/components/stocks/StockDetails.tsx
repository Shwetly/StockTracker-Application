import React, { useState, useEffect } from 'react';
import { ArrowLeft, Star, TrendingUp, TrendingDown, BarChart3 } from 'lucide-react';
import { Stock, StockQuote, ChartDataPoint } from '../../types/stock';
import { StockApiService } from '../../services/api';
import { StockChart } from './StockChart';
import { LoadingSpinner } from '../ui/LoadingSpinner';
import { ErrorMessage } from '../ui/ErrorMessage';

interface StockDetailsProps {
  symbol: string;
  name: string;
  onBack: () => void;
  onWatchlistToggle: (symbol: string) => void;
  isInWatchlist: boolean;
}

export function StockDetails({ 
  symbol, 
  name, 
  onBack, 
  onWatchlistToggle, 
  isInWatchlist 
}: StockDetailsProps) {
  const [quote, setQuote] = useState<StockQuote | null>(null);
  const [chartData, setChartData] = useState<ChartDataPoint[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadStockData();
  }, [symbol]);

  const loadStockData = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const [quoteData, timeSeriesData] = await Promise.all([
        StockApiService.getStockQuote(symbol),
        StockApiService.getStockTimeSeries(symbol, 'daily')
      ]);

      setQuote(quoteData);

      // Process time series data for chart
      const timeSeries = timeSeriesData['Time Series (Daily)'];
      if (timeSeries) {
        const chartPoints: ChartDataPoint[] = Object.entries(timeSeries)
          .slice(0, 30) // Last 30 days
          .reverse()
          .map(([date, data]) => ({
            date,
            price: parseFloat(data['4. close']),
            volume: parseInt(data['5. volume'])
          }));
        setChartData(chartPoints);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load stock data');
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-12">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <ErrorMessage
        message={error}
        onRetry={loadStockData}
      />
    );
  }

  if (!quote) {
    return (
      <ErrorMessage
        message="Stock data not available"
        onRetry={loadStockData}
      />
    );
  }

  const price = parseFloat(quote['05. price']);
  const change = parseFloat(quote['09. change']);
  const changePercent = parseFloat(quote['10. change percent'].replace('%', ''));
  const isPositive = change >= 0;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <button
          onClick={onBack}
          className="flex items-center gap-2 px-4 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          Back to Search
        </button>

        <button
          onClick={() => onWatchlistToggle(symbol)}
          className={`flex items-center gap-2 px-4 py-2 rounded-lg transition-colors ${
            isInWatchlist
              ? 'bg-yellow-100 dark:bg-yellow-900 text-yellow-800 dark:text-yellow-200'
              : 'bg-gray-100 dark:bg-gray-800 text-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-700'
          }`}
        >
          <Star className={`w-4 h-4 ${isInWatchlist ? 'fill-current' : ''}`} />
          {isInWatchlist ? 'Remove from Watchlist' : 'Add to Watchlist'}
        </button>
      </div>

      {/* Stock Info */}
      <div className="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-200 dark:border-gray-700">
        <div className="flex items-start justify-between mb-6">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              {symbol}
            </h1>
            <p className="text-lg text-gray-600 dark:text-gray-400">
              {name}
            </p>
          </div>
          <div className={`flex items-center gap-2 ${isPositive ? 'text-green-600' : 'text-red-600'}`}>
            {isPositive ? (
              <TrendingUp className="w-6 h-6" />
            ) : (
              <TrendingDown className="w-6 h-6" />
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div className="space-y-2">
            <p className="text-sm text-gray-600 dark:text-gray-400">Current Price</p>
            <p className="text-3xl font-bold text-gray-900 dark:text-white">
              ${price.toFixed(2)}
            </p>
          </div>

          <div className="space-y-2">
            <p className="text-sm text-gray-600 dark:text-gray-400">Change</p>
            <div className={`${isPositive ? 'text-green-600' : 'text-red-600'}`}>
              <p className="text-xl font-semibold">
                {isPositive ? '+' : ''}${change.toFixed(2)}
              </p>
              <p className="text-sm">
                ({isPositive ? '+' : ''}{changePercent.toFixed(2)}%)
              </p>
            </div>
          </div>

          <div className="space-y-2">
            <p className="text-sm text-gray-600 dark:text-gray-400">Volume</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">
              {parseInt(quote['06. volume']).toLocaleString()}
            </p>
          </div>

          <div className="space-y-2">
            <p className="text-sm text-gray-600 dark:text-gray-400">Last Updated</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">
              {new Date(quote['07. latest trading day']).toLocaleDateString()}
            </p>
          </div>
        </div>
      </div>

      {/* Chart */}
      {chartData.length > 0 && (
        <StockChart data={chartData} symbol={symbol} />
      )}
    </div>
  );
}