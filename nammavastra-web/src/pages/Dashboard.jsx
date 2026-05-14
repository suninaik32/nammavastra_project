import React from 'react';
import { Package, TrendingUp, IndianRupee } from 'lucide-react';

export default function Dashboard() {
  return (
    <div>
      <h1 className="page-title">Namaskara, Lakshmi Silk Weavers</h1>
      <p className="page-subtitle">Here is your weaving enterprise at a glance.</p>

      <div className="grid-3" style={{ marginBottom: '40px' }}>
        <div className="card stat-card">
          <div className="stat-icon">
            <Package size={28} />
          </div>
          <div className="stat-content">
            <h3>Total Sarees</h3>
            <p>124</p>
          </div>
        </div>
        
        <div className="card stat-card">
          <div className="stat-icon" style={{ backgroundColor: 'rgba(201, 148, 10, 0.1)', color: 'var(--secondary-color)' }}>
            <IndianRupee size={28} />
          </div>
          <div className="stat-content">
            <h3>Avg Price</h3>
            <p>₹12,500</p>
          </div>
        </div>

        <div className="card stat-card">
          <div className="stat-icon" style={{ backgroundColor: 'rgba(11, 110, 79, 0.1)', color: 'var(--tertiary-color)' }}>
            <TrendingUp size={28} />
          </div>
          <div className="stat-content">
            <h3>Top Category</h3>
            <p>Silk</p>
          </div>
        </div>
      </div>

      <h2 style={{ marginBottom: '16px', color: 'var(--text-primary)' }}>Featured Trend</h2>
      <div className="card" style={{ 
        backgroundImage: 'linear-gradient(to right, rgba(139, 26, 74, 0.9), rgba(139, 26, 74, 0.4)), url(https://images.unsplash.com/photo-1610189014164-85012354c0e0?q=80&w=800)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '240px',
        color: 'white',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'flex-end',
        marginBottom: '40px'
      }}>
        <span style={{ fontSize: '14px', textTransform: 'uppercase', letterSpacing: '1px', marginBottom: '8px' }}>Trending Now</span>
        <h2 style={{ fontSize: '32px' }}>Festive Banarasi Collection</h2>
      </div>

      <h2 style={{ marginBottom: '16px', color: 'var(--text-primary)' }}>The Weaver's Story</h2>
      <div className="card" style={{ backgroundColor: 'var(--surface-color-variant)', borderLeft: '6px solid var(--primary-color)' }}>
        <h3 style={{ color: 'var(--primary-color)', marginBottom: '12px' }}>Ilkal & Molakalmuru Heritage</h3>
        <p style={{ lineHeight: '1.6', color: 'var(--text-secondary)' }}>
          The legendary red "Tope Teni" pallu of Ilkal and the rich silk motifs of Molakalmuru are woven with generations of history. 
          This platform bridges our ancient looms directly with modern global fashion, ensuring the artisanal craft thrives in the digital age.
        </p>
      </div>
    </div>
  );
}
