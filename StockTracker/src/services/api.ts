const API_KEY = 'demo'; // Replace with your Alpha Vantage API key
const BASE_URL = 'https://www.alphavantage.co/query';

// Cache implementation
interface CacheItem<T> {
  data: T;
  timestamp: number;
  expiry: number;
}

class ApiCache {
  private cache = new Map<string, CacheItem<any>>();
  private readonly DEFAULT_EXPIRY = 5 * 60 * 1000; // 5 minutes

  set<T>(key: string, data: T, expiry = this.DEFAULT_EXPIRY): void {
    this.cache.set(key, {
      data,
      timestamp: Date.now(),
      expiry
    });
  }

  get<T>(key: string): T | null {
    const item = this.cache.get(key);
    if (!item) return null;

    if (Date.now() - item.timestamp > item.expiry) {
      this.cache.delete(key);
      return null;
    }

    return item.data;
  }

  clear(): void {
    this.cache.clear();
  }
}

const cache = new ApiCache();

export class StockApiService {
  private static async makeRequest(params: Record<string, string>) {
    const url = new URL(BASE_URL);
    Object.entries(params).forEach(([key, value]) => {
      url.searchParams.append(key, value);
    });

    const response = await fetch(url.toString());
    if (!response.ok) {
      throw new Error(`API request failed: ${response.statusText}`);
    }

    return response.json();
  }

  static async searchStocks(query: string) {
    const cacheKey = `search_${query}`;
    const cached = cache.get(cacheKey);
    if (cached) return cached;

    try {
      const data = await this.makeRequest({
        function: 'SYMBOL_SEARCH',
        keywords: query,
        apikey: API_KEY
      });

      const results = data.bestMatches || [];
      cache.set(cacheKey, results, 10 * 60 * 1000); // 10 minutes cache
      return results;
    } catch (error) {
      console.error('Search error:', error);
      throw new Error('Failed to search stocks');
    }
  }

  static async getStockQuote(symbol: string) {
    const cacheKey = `quote_${symbol}`;
    const cached = cache.get(cacheKey);
    if (cached) return cached;

    try {
      const data = await this.makeRequest({
        function: 'GLOBAL_QUOTE',
        symbol: symbol,
        apikey: API_KEY
      });

      const quote = data['Global Quote'];
      if (!quote) {
        throw new Error('Stock not found');
      }

      cache.set(cacheKey, quote, 1 * 60 * 1000); // 1 minute cache
      return quote;
    } catch (error) {
      console.error('Quote error:', error);
      throw new Error('Failed to fetch stock quote');
    }
  }

  static async getStockTimeSeries(symbol: string, interval: 'daily' | 'weekly' | 'monthly' = 'daily') {
    const cacheKey = `timeseries_${symbol}_${interval}`;
    const cached = cache.get(cacheKey);
    if (cached) return cached;

    try {
      const functionMap = {
        daily: 'TIME_SERIES_DAILY',
        weekly: 'TIME_SERIES_WEEKLY',
        monthly: 'TIME_SERIES_MONTHLY'
      };

      const data = await this.makeRequest({
        function: functionMap[interval],
        symbol: symbol,
        apikey: API_KEY
      });

      cache.set(cacheKey, data, 5 * 60 * 1000); // 5 minutes cache
      return data;
    } catch (error) {
      console.error('Time series error:', error);
      throw new Error('Failed to fetch stock data');
    }
  }

  static async getTopGainersLosers() {
    const cacheKey = 'top_gainers_losers';
    const cached = cache.get(cacheKey);
    if (cached) return cached;

    try {
      const data = await this.makeRequest({
        function: 'TOP_GAINERS_LOSERS',
        apikey: API_KEY
      });

      cache.set(cacheKey, data, 15 * 60 * 1000); // 15 minutes cache
      return data;
    } catch (error) {
      console.error('Top gainers/losers error:', error);
      throw new Error('Failed to fetch market data');
    }
  }
}