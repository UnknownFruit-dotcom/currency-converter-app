import { useState, useEffect } from 'react'
import './App.css'
import { currenciesService } from "./api/currenciesService";
import { conversionService } from "./api/conversionService";
import { ToastContainer, toast } from 'react-toastify';
import info from './assets/info.png';

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
                setError('Error loading currencies');
            }
        };

        loadCurrencies();
    }, []);

    const handleConversionSubmission = async (event) => {
        setError('');
        setLoading(true);
        event.preventDefault();

        if (!amount.match(/^-?\d*\.?\d+$/)) {
            setError("Incorrect number format");
            setLoading(false);
            setResult("");
            return;
        }

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
                  <div className="formHeader">
                      <h1>Conversion</h1>
                      <img src={info} alt="Info" title="Rates update daily from the Central Bank. Figures may be approximate.

Курсы обновляются раз в день по данным ЦБ. Возможны неточности."/>
                  </div>

                  <select id="fromCurrency"
                      value={fromId}
                      title="Source currency"

                      onChange={(e) => {
                      setFromId(e.target.value);
                      setResult('');
                  }}>
                      {
                          currencies.map(currency => (
                              <option data-code={currency.code} key={currency.id} value={currency.id}>{currency.name}</option>
                          ))
                      }
                  </select>

                  <select id="toCurrency"
                      value={toId}
                      title="Target currency"

                      onChange={(e) => {
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

                  <input type="text" className="amount" value={amount}
                      title="Amount to convert (dot as decimal separator)"
                      onChange={(e) => setAmount(e.target.value)} required />

                  <div className="result">
                      {result + " " + toCode}
                  </div>

                  <button type="submit" className="conversionBtn">Convert</button>

                  <div className={`message${error ? "-error" : ""}`}>
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
