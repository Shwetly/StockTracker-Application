import React, { useState, useEffect } from 'react';
import { X, Star, TrendingUp, TrendingDown } from 'lucide-react';
import { Stock, StockQuote } from '../../types/stock';
import { StockApiService } from '../../services/api';
import { LoadingSpinner } from '../ui/LoadingSpinner';
import { EmptyState } from '../ui/EmptyState';

interface WatchlistModalProps {
  isOpen: boolean;
  onClose: () => void;
  watchlist: string[];
  onRemoveFromWatchlist: (symbol: string) => void;
  onStockClick: (stock: Stock) => void;
}

export function WatchlistModal({
  isOpen,
  onClose,
  watchlist,
  onRemoveFromWatchlist,
  onStockClick
}: WatchlistModalProps) {
  const [stocks, setStocks] = useState<Stock[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (isOpen && watchlist.length > 0) {
      loadWatchlistData();
    }
  }, [isOpen, watchlist]);

  const loadWatchlistData = async () => {
    setIsLoading(true);
    try {
      const stockPromises = watchlist.map(async (symbol) => {
        try {
          const quote: StockQuote = await StockApiService.getStockQuote(symbol);
          return {
            symbol: quote['01. symbol'],
            name: symbol, // We don't have the full name in the quote
            price: parseFloat(quote['05. price']),
            change: parseFloat(quote['09. change']),
            changePercent: parseFloat(quote['10. change percent'].replace('%', '')),
            volume: parseInt(quote['06. volume'])
          };
        } catch (error) {
          console.error(`Failed to load ${symbol}:`, error);
          return null;
        }
      });

      const results = await Promise.all(stockPromises);
      setStocks(results.filter((stock): stock is Stock => stock !== null));
    } catch (error) {
      console.error('Failed to load watchlist:', error);
    } finally {
      setIsLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white dark:bg-gray-900 rounded-xl max-w-4xl w-full max-h-[80vh] overflow-hidden">
        <div className="flex items-center justify-between p-6 border-b border-gray-200 dark:border-gray-700">
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
            My Watchlist
          </h2>
          <button
            onClick={onClose}
            className="p-2 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 transition-colors"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        <div className="p-6 overflow-y-auto max-h-[calc(80vh-120px)]">
          {isLoading ? (
            <div className="flex items-center justify-center py-12">
              <LoadingSpinner size="lg" />
            </div>
          ) : watchlist.length === 0 ? (
            <EmptyState
              icon={<Star className="w-12 h-12 text-gray-400" />}
              title="No stocks in watchlist"
              description="Add stocks to your watchlist to track them here"
            />
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {stocks.map((stock) => {
                const isPositive = stock.change >= 0;
                return (
                  <div
                    key={stock.symbol}
                    className="bg-gray-50 dark:bg-gray-800 rounded-lg p-4 border border-gray-200 dark:border-gray-700 hover:shadow-md transition-shadow cursor-pointer"
                    onClick={() => {
                      onStockClick(stock);
                      onClose();
                    }}
                  >
                    <div className="flex items-start justify-between mb-3">
                      <div>
                        <h3 className="font-bold text-lg text-gray-900 dark:text-white">
                          {stock.symbol}
                        </h3>
                        <p className="text-sm text-gray-600 dark:text-gray-400">
                          {stock.name}
                        </p>
                      </div>
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onRemoveFromWatchlist(stock.symbol);
                        }}
                        className="p-1 text-yellow-500 hover:text-yellow-600 transition-colors"
                      >
                        <Star className="w-4 h-4 fill-current" />
                      </button>
                    </div>

                    <div className="flex items-center justify-between">
                      <span className="text-xl font-bold text-gray-900 dark:text-white">
                        ${stock.price.toFixed(2)}
                      </span>
                      <div className={`flex items-center gap-1 ${isPositive ? 'text-green-600' : 'text-red-600'}`}>
                        {isPositive ? (
                          <TrendingUp className="w-4 h-4" />
                        ) : (
                          <TrendingDown className="w-4 h-4" />
                        )}
                        <span className="font-semibold">
                          {isPositive ? '+' : ''}${stock.change.toFixed(2)}
                        </span>
                        <span className="text-sm">
                          ({isPositive ? '+' : ''}{stock.changePercent.toFixed(2)}%)
                        </span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}