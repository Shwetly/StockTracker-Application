import React from 'react';
import { TrendingUp, Moon, Sun, Star } from 'lucide-react';
import { useTheme } from '../../hooks/useTheme';

interface HeaderProps {
  onWatchlistClick: () => void;
}

export function Header({ onWatchlistClick }: HeaderProps) {
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-3">
            <div className="flex items-center justify-center w-10 h-10 bg-blue-600 rounded-lg">
              <TrendingUp className="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 className="text-xl font-bold text-gray-900 dark:text-white">
                StockTracker
              </h1>
              <p className="text-xs text-gray-500 dark:text-gray-400">
                Real-time market data
              </p>
            </div>
          </div>

          <div className="flex items-center gap-4">
            <button
              onClick={onWatchlistClick}
              className="flex items-center gap-2 px-4 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors"
            >
              <Star className="w-4 h-4" />
              <span className="hidden sm:inline">Watchlist</span>
            </button>
            
            <button
              onClick={toggleTheme}
              className="p-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors"
              aria-label="Toggle theme"
            >
              {theme === 'light' ? (
                <Moon className="w-5 h-5" />
              ) : (
                <Sun className="w-5 h-5" />
              )}
            </button>
          </div>
        </div>
      </div>
    </header>
  );
}