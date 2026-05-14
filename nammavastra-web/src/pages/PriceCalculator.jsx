import React, { useState } from 'react';
import { IndianRupee } from 'lucide-react';

export default function PriceCalculator() {
  const [materialCost, setMaterialCost] = useState(5000);
  const [profitMargin, setProfitMargin] = useState(40);

  const profitAmount = (materialCost * profitMargin) / 100;
  const suggestedPrice = materialCost + profitAmount;

  return (
    <div>
      <h1 className="page-title">Price Calculator</h1>
      <p className="page-subtitle">Determine the perfect selling price based on material costs.</p>

      <div className="grid-2">
        <div className="card">
          <h3 style={{ marginBottom: '24px' }}>Calculation Inputs</h3>
          
          <div style={{ marginBottom: '24px' }}>
            <label style={{ display: 'block', marginBottom: '8px', fontWeight: 500 }}>Material Cost (₹)</label>
            <input 
              type="number" 
              value={materialCost}
              onChange={(e) => setMaterialCost(Number(e.target.value))}
              style={{
                width: '100%',
                padding: '12px 16px',
                borderRadius: '8px',
                border: '1px solid #ddd',
                fontSize: '16px'
              }}
            />
          </div>

          <div style={{ marginBottom: '24px' }}>
            <label style={{ display: 'block', marginBottom: '8px', fontWeight: 500 }}>
              Profit Margin: {profitMargin}%
            </label>
            <input 
              type="range" 
              min="10" max="200" 
              value={profitMargin}
              onChange={(e) => setProfitMargin(Number(e.target.value))}
              style={{ width: '100%', accentColor: 'var(--primary-color)' }}
            />
          </div>
        </div>

        <div className="card" style={{ backgroundColor: 'var(--primary-color)', color: 'white', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', textAlign: 'center' }}>
          <h3 style={{ opacity: 0.9, marginBottom: '8px', fontWeight: 400 }}>Suggested Selling Price</h3>
          <h1 style={{ fontSize: '48px', marginBottom: '24px', display: 'flex', alignItems: 'center' }}>
            <IndianRupee size={40} /> {suggestedPrice.toLocaleString()}
          </h1>
          
          <div style={{ display: 'flex', gap: '32px', opacity: 0.8 }}>
            <div>
              <div style={{ fontSize: '12px', textTransform: 'uppercase' }}>Cost</div>
              <div style={{ fontSize: '20px', fontWeight: 600 }}>₹{materialCost.toLocaleString()}</div>
            </div>
            <div>
              <div style={{ fontSize: '12px', textTransform: 'uppercase' }}>Profit</div>
              <div style={{ fontSize: '20px', fontWeight: 600 }}>₹{profitAmount.toLocaleString()}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
