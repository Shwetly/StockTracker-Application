import React, { useState } from 'react';
import { Header } from './components/layout/Header';
import { SearchBar } from './components/search/SearchBar';
import { StockDetails } from './components/stocks/StockDetails';
import { WatchlistModal } from './components/watchlist/WatchlistModal';
import { useLocalStorage } from './hooks/useLocalStorage';
import { Stock } from './types/stock';
import { TrendingUp } from 'lucide-react';

function App() {
  const [selectedStock, setSelectedStock] = useState<{ symbol: string; name: string } | null>(null);
  const [watchlist, setWatchlist] = useLocalStorage<string[]>('stockWatchlist', []);
  const [isWatchlistOpen, setIsWatchlistOpen] = useState(false);

  const handleStockSelect = (symbol: string, name: string) => {
    setSelectedStock({ symbol, name });
  };

  const handleBackToSearch = () => {
    setSelectedStock(null);
  };

  const handleWatchlistToggle = (symbol: string) => {
    setWatchlist(prev => 
      prev.includes(symbol)
        ? prev.filter(s => s !== symbol)
        : [...prev, symbol]
    );
  };

  const handleStockClick = (stock: Stock) => {
    setSelectedStock({ symbol: stock.symbol, name: stock.name });
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors">
      <Header onWatchlistClick={() => setIsWatchlistOpen(true)} />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {selectedStock ? (
          <StockDetails
            symbol={selectedStock.symbol}
            name={selectedStock.name}
            onBack={handleBackToSearch}
            onWatchlistToggle={handleWatchlistToggle}
            isInWatchlist={watchlist.includes(selectedStock.symbol)}
          />
        ) : (
          <div className="space-y-8">
            {/* Hero Section */}
            <div className="text-center space-y-4">
              <div className="flex items-center justify-center gap-3 mb-4">
                <div className="flex items-center justify-center w-16 h-16 bg-blue-600 rounded-2xl">
                  <TrendingUp className="w-8 h-8 text-white" />
                </div>
              </div>
              <h1 className="text-4xl md:text-5xl font-bold text-gray-900 dark:text-white">
                Track Your Investments
              </h1>
              <p className="text-xl text-gray-600 dark:text-gray-400 max-w-2xl mx-auto">
                Get real-time stock quotes, detailed charts, and manage your watchlist all in one place
              </p>
            </div>

            {/* Search Section */}
            <div className="max-w-2xl mx-auto">
              <SearchBar onStockSelect={handleStockSelect} />
            </div>

            {/* Features */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-16">
              <div className="text-center space-y-4">
                <div className="flex items-center justify-center w-12 h-12 bg-blue-100 dark:bg-blue-900 rounded-xl mx-auto">
                  <TrendingUp className="w-6 h-6 text-blue-600 dark:text-blue-400" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                  Real-time Data
                </h3>
                <p className="text-gray-600 dark:text-gray-400">
                  Get up-to-date stock prices and market information powered by Alpha Vantage
                </p>
              </div>

              <div className="text-center space-y-4">
                <div className="flex items-center justify-center w-12 h-12 bg-green-100 dark:bg-green-900 rounded-xl mx-auto">
                  <TrendingUp className="w-6 h-6 text-green-600 dark:text-green-400" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                  Interactive Charts
                </h3>
                <p className="text-gray-600 dark:text-gray-400">
                  Visualize stock performance with beautiful, interactive price charts
                </p>
              </div>

              <div className="text-center space-y-4">
                <div className="flex items-center justify-center w-12 h-12 bg-purple-100 dark:bg-purple-900 rounded-xl mx-auto">
                  <TrendingUp className="w-6 h-6 text-purple-600 dark:text-purple-400" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                  Personal Watchlist
                </h3>
                <p className="text-gray-600 dark:text-gray-400">
                  Save and track your favorite stocks in a personalized watchlist
                </p>
              </div>
            </div>
          </div>
        )}
      </main>

      <WatchlistModal
        isOpen={isWatchlistOpen}
        onClose={() => setIsWatchlistOpen(false)}
        watchlist={watchlist}
        onRemoveFromWatchlist={handleWatchlistToggle}
        onStockClick={handleStockClick}
      />
    </div>
  );
}

export default App;