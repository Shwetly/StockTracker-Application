import React from 'react';
import { Search, TrendingUp } from 'lucide-react';

interface EmptyStateProps {
  icon?: React.ReactNode;
  title: string;
  description: string;
  action?: React.ReactNode;
  className?: string;
}

export function EmptyState({ 
  icon, 
  title, 
  description, 
  action, 
  className = '' 
}: EmptyStateProps) {
  const defaultIcon = <Search className="w-12 h-12 text-gray-400" />;

  return (
    <div className={`flex flex-col items-center justify-center p-8 text-center ${className}`}>
      {icon || defaultIcon}
      <h3 className="text-lg font-semibold text-gray-900 dark:text-white mt-4 mb-2">
        {title}
      </h3>
      <p className="text-gray-600 dark:text-gray-400 mb-4 max-w-md">
        {description}
      </p>
      {action}
    </div>
  );
}