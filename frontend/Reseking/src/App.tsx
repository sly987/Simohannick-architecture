import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";

function App() {
  const [count, setCount] = useState(0);
  const [text, setText] = useState("");

  const callApi = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/test", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ value: text }),
      });

      if (!response.ok) {
        throw new Error("Erreur lors de l'appel API");
      }

      console.log("API appelée avec succès");
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>

      <h1>Vite + React</h1>

      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>

        <br /><br />

        <input
          type="text"
          value={text}
          placeholder="Envoyer un texte à l'API"
          onChange={(e) => setText(e.target.value)}
        />

        <br /><br />

        <button onClick={callApi}>
          Appeler l’API
        </button>
      </div>

      <p className="read-the-docs">
        Edit <code>src/App.tsx</code> and save to test HMR
      </p>
    </>
  );
}

export default App;
