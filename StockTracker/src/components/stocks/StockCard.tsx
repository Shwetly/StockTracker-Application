import React from 'react';
import { TrendingUp, TrendingDown, Star } from 'lucide-react';
import { Stock } from '../../types/stock';

interface StockCardProps {
  stock: Stock;
  onClick: () => void;
  onWatchlistToggle: (symbol: string) => void;
  isInWatchlist: boolean;
  className?: string;
}

export function StockCard({ 
  stock, 
  onClick, 
  onWatchlistToggle, 
  isInWatchlist, 
  className = '' 
}: StockCardProps) {
  const isPositive = stock.change >= 0;

  const handleWatchlistClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    onWatchlistToggle(stock.symbol);
  };

  return (
    <div
      onClick={onClick}
      className={`bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-200 dark:border-gray-700 hover:shadow-lg hover:border-blue-300 dark:hover:border-blue-600 transition-all cursor-pointer group ${className}`}
    >
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1">
          <div className="flex items-center gap-2 mb-1">
            <h3 className="font-bold text-lg text-gray-900 dark:text-white">
              {stock.symbol}
            </h3>
            <button
              onClick={handleWatchlistClick}
              className={`p-1 rounded-full transition-colors ${
                isInWatchlist
                  ? 'text-yellow-500 hover:text-yellow-600'
                  : 'text-gray-400 hover:text-yellow-500'
              }`}
            >
              <Star className={`w-4 h-4 ${isInWatchlist ? 'fill-current' : ''}`} />
            </button>
          </div>
          <p className="text-sm text-gray-600 dark:text-gray-400 truncate">
            {stock.name}
          </p>
        </div>
        <div className={`flex items-center gap-1 ${isPositive ? 'text-green-600' : 'text-red-600'}`}>
          {isPositive ? (
            <TrendingUp className="w-4 h-4" />
          ) : (
            <TrendingDown className="w-4 h-4" />
          )}
        </div>
      </div>

      <div className="space-y-2">
        <div className="flex items-baseline justify-between">
          <span className="text-2xl font-bold text-gray-900 dark:text-white">
            ${stock.price.toFixed(2)}
          </span>
          <div className={`text-right ${isPositive ? 'text-green-600' : 'text-red-600'}`}>
            <div className="font-semibold">
              {isPositive ? '+' : ''}${stock.change.toFixed(2)}
            </div>
            <div className="text-sm">
              ({isPositive ? '+' : ''}{stock.changePercent.toFixed(2)}%)
            </div>
          </div>
        </div>

        {stock.volume && (
          <div className="flex justify-between text-sm text-gray-600 dark:text-gray-400">
            <span>Volume</span>
            <span>{stock.volume.toLocaleString()}</span>
          </div>
        )}
      </div>
    </div>
  );
}