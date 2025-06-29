import React from 'react';
import { Stock } from '../../types/stock';
import { StockCard } from './StockCard';
import { LoadingSpinner } from '../ui/LoadingSpinner';
import { ErrorMessage } from '../ui/ErrorMessage';
import { EmptyState } from '../ui/EmptyState';

interface StockGridProps {
  stocks: Stock[];
  isLoading: boolean;
  error: string | null;
  onStockClick: (stock: Stock) => void;
  onWatchlistToggle: (symbol: string) => void;
  watchlist: string[];
  onRetry?: () => void;
  emptyMessage?: string;
  className?: string;
}

export function StockGrid({
  stocks,
  isLoading,
  error,
  onStockClick,
  onWatchlistToggle,
  watchlist,
  onRetry,
  emptyMessage = "No stocks to display",
  className = ''
}: StockGridProps) {
  if (isLoading) {
    return (
      <div className={`flex items-center justify-center py-12 ${className}`}>
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <ErrorMessage
        message={error}
        onRetry={onRetry}
        className={className}
      />
    );
  }

  if (stocks.length === 0) {
    return (
      <EmptyState
        title="No stocks found"
        description={emptyMessage}
        className={className}
      />
    );
  }

  return (
    <div className={`grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 ${className}`}>
      {stocks.map((stock) => (
        <StockCard
          key={stock.symbol}
          stock={stock}
          onClick={() => onStockClick(stock)}
          onWatchlistToggle={onWatchlistToggle}
          isInWatchlist={watchlist.includes(stock.symbol)}
        />
      ))}
    </div>
  );
}