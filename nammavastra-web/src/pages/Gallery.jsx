import React, { useState } from 'react';
import { Upload, MessageCircle } from 'lucide-react';

const INITIAL_SAREES = [
  { id: 1, title: 'Kanjivaram Blue', category: 'Silk', img: 'https://images.unsplash.com/photo-1610189014164-85012354c0e0?q=80&w=500' },
  { id: 2, title: 'Cotton Block Print', category: 'Cotton', img: 'https://images.unsplash.com/photo-1583391733959-b0eb97bf5d04?q=80&w=500' },
  { id: 3, title: 'Mysore Crepe', category: 'Crepe', img: 'https://images.unsplash.com/photo-1601309503468-b7c126db0f93?q=80&w=500' },
  { id: 4, title: 'Gadwal Silk', category: 'Silk', img: 'https://images.unsplash.com/photo-1589465885857-44edb59bbff2?q=80&w=500' }
];

export default function Gallery() {
  const [sarees, setSarees] = useState(INITIAL_SAREES);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const newSaree = {
        id: Date.now(),
        title: file.name.split('.')[0], // Use filename as title
        category: 'New Upload',
        img: URL.createObjectURL(file) // Create a local preview URL
      };
      
      setSarees([newSaree, ...sarees]);
      alert(`Success! "${file.name}" has been added to your gallery.`);
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
        <div>
          <h1 className="page-title" style={{ marginBottom: 0 }}>My Gallery</h1>
          <p className="page-subtitle" style={{ marginBottom: 0 }}>Manage your entire digital inventory.</p>
        </div>
        
        <input 
          type="file" 
          id="saree-upload" 
          style={{ display: 'none' }} 
          accept="image/*"
          onChange={handleFileUpload}
        />
        
        <button className="btn-primary" onClick={() => document.getElementById('saree-upload').click()}>
          <Upload size={20} />
          Upload Saree
        </button>
      </div>

      <div className="grid-4">
        {sarees.map(saree => (
          <div key={saree.id} className="saree-card">
            <img src={saree.img} alt={saree.title} className="saree-img" />
            <div className="saree-info">
              <div className="saree-title">{saree.title}</div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '8px' }}>
                <span className="saree-category">{saree.category}</span>
                <a 
                  href={`https://api.whatsapp.com/send?text=${encodeURIComponent(`Hello! I am interested in inquiring about the ${saree.title} handloom saree I saw on Namma-Vastra.`)}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  style={{ color: '#25D366', display: 'flex', alignItems: 'center', gap: '4px', textDecoration: 'none', fontSize: '14px', fontWeight: 600 }}
                >
                  <MessageCircle size={18} />
                  Inquire
                </a>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
