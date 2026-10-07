import { useState, useEffect } from 'react'
import './App.css'
import { currenciesService } from "./api/currenciesService";
import { conversionService } from "./api/conversionService";
import { ToastContainer, toast } from 'react-toastify';

function App() {
    const [currencies, setCurrencies] = useState([]);
    const [fromId, setFromId] = useState('');
    const [toId, setToId] = useState('');
    const [toCode, setToCode] = useState('');
    const [amount, setAmount] = useState("100.0");

    const [result, setResult] = useState('');
    const [error, setError] = useState('');

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const loadCurrencies = async () => {
            try {
                const data = await currenciesService.getAllCurrencies();
                setCurrencies(data);

                if (data.length > 0) {
                    setFromId(String(data[0].id));
                    setToId(String(data[0].id));
                    setToCode(data[0].code);
                }

                setError('');
            } catch (e) {
                console.error(e);
                setCurrencies([]);
            }
        };

        loadCurrencies();
    }, []);

    const handleConversionSubmission = async (event) => {
        setError('');
        setLoading(true);
        event.preventDefault();
        try {
            const data = await conversionService.convert(fromId, toId, amount);
            setResult(data);
            toast.success("Conversion successful");
        } catch (e) {
            console.error(e);
            setError('Error. Please, try refreshing the page.');
            toast.error("Error encountered");
        } finally {
            setLoading(false);
        }
    };

  return (
    <>
      <section id="center">
            <form className="conversionForm" onSubmit={handleConversionSubmission}>
                  <h1>Conversion</h1>

                  <select id="fromCurrency" onChange={(e) => {
                      setFromId(e.target.value);
                      setResult('');
                  }}>
                      {
                          currencies.map(currency => (
                              <option data-code={currency.code} key={currency.id} value={currency.id}>{currency.name}</option>
                          ))
                      }
                  </select>

                  <select id="toCurrency" onChange={(e) => {
                      setToId(e.target.value);
                      const code = e.target.selectedOptions[0]?.dataset.code;
                      setToCode(code ?? "");
                      setResult('');
                  }}>
                      {
                          currencies.map(currency => (
                              <option data-code={currency.code} key={currency.id} value={currency.id}>{currency.name}</option>
                          ))
                      }
                  </select>

                  <input type="text" id="amount" value={amount} onChange={(e) => setAmount(e.target.value)}/>

                  <div className="result">
                      {result + " " + toCode}
                  </div>

                  <button type="submit" className="conversionBtn">Convert</button>

                  <div className="message">
                      {error}
                      {loading && "One second..."}
                  </div>
            </form>
      </section>

      <ToastContainer />
    </>
  )
}

export default App
