import React, { useState } from 'react';
import axios from 'axios';

export default function App() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  async function send() {
    if (!input.trim()) return;
    const userMsg = { role: 'user', content: input };
    setMessages(m => [...m, { from: 'you', text: input }]);
    setInput('');
    setLoading(true);
    try {
      const res = await axios.post('/api/chat', { message: input });
      const reply = res.data?.reply ?? 'No reply';
      setMessages(m => [...m, { from: 'assistant', text: reply }]);
    } catch (err) {
      console.error(err);
      setMessages(m => [...m, { from: 'assistant', text: 'Error contacting server' }]);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{maxWidth:800, margin:'40px auto', fontFamily:'system-ui,Arial'}}>
      <h1>LLM Chat — React frontend + Java backend</h1>
      <div style={{border:'1px solid #ddd', padding:12, borderRadius:8, minHeight:300}}>
        {messages.map((m,i) => (
          <div key={i} style={{marginBottom:10}}>
            <strong>{m.from}:</strong> <span>{m.text}</span>
          </div>
        ))}
        {loading && <div><em>Waiting for response...</em></div>}
      </div>
      <div style={{marginTop:12, display:'flex', gap:8}}>
        <input value={input} onChange={e=>setInput(e.target.value)} style={{flex:1, padding:8}} placeholder="Type a message..." />
        <button onClick={send} disabled={loading} style={{padding:'8px 12px'}}>Send</button>
      </div>
    </div>
  );
}
