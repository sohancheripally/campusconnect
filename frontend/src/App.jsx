import Dashboard from "./Dashboard";
import { useState } from "react";
import "./App.css";

const roles = ["Student", "Faculty", "Admin"];

function App() {
  const [role, setRole] = useState("Student");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(true);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [user, setUser] = useState(null);

  async function handleLogin(event) {
    event.preventDefault();
    setMessage("");
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Invalid email or password.");
      }

      if (data.role !== role.toUpperCase()) {
        throw new Error(`This account belongs to the ${data.role.toLowerCase()} role. Please select the correct role.`);
      }

      if (remember) {
        localStorage.setItem("campusconnect_token", data.token);
        localStorage.setItem("campusconnect_user", JSON.stringify(data));
      } else {
        sessionStorage.setItem("campusconnect_token", data.token);
        sessionStorage.setItem("campusconnect_user", JSON.stringify(data));
      }

      setUser(data);
      setMessage("Login successful!");
    } catch (error) {
      setMessage(error.message || "Unable to connect. Check that the backend is running.");
    } finally {
      setLoading(false);
    }
  }

  function logout() {
    localStorage.removeItem("campusconnect_token");
    localStorage.removeItem("campusconnect_user");
    sessionStorage.removeItem("campusconnect_token");
    sessionStorage.removeItem("campusconnect_user");
    setUser(null);
    setEmail("");
    setPassword("");
    setMessage("");
  }

 if (user) {
  return <Dashboard user={user} onLogout={logout} />;
}

  return (
    <main className="page-shell">
      <section className="intro-panel">
        <a className="brand brand-light" href="#home">
          <span className="brand-icon">C</span>
          Campus<span>Connect</span>
        </a>

        <div className="intro-content">
          <span className="eyebrow"><span className="status-dot" /> THE SMART CAMPUS EXPERIENCE</span>
          <h1>One campus.<br />Endless <span>possibilities.</span></h1>
          <p className="intro-description">
            Your academics, campus updates, events and community — all connected in one place.
          </p>

          <div className="feature-list">
            <div className="feature">
              <div className="feature-icon">✦</div>
              <div><h3>Everything in one place</h3><p>Access the tools and updates you need for campus life.</p></div>
            </div>
            <div className="feature">
              <div className="feature-icon">↗</div>
              <div><h3>Stay in the loop</h3><p>Keep up with notices, events and academic activities.</p></div>
            </div>
            <div className="feature">
              <div className="feature-icon">⌘</div>
              <div><h3>Built for your campus</h3><p>A connected experience for students, faculty and admins.</p></div>
            </div>
          </div>
        </div>

        <div className="intro-footer">
          <div className="avatar-stack"><span>S</span><span>F</span><span>A</span></div>
          <p>One community. <strong>One CampusConnect.</strong></p>
        </div>
        <div className="orb orb-one" />
        <div className="orb orb-two" />
      </section>

      <section className="login-panel">
        <div className="login-topline">
          <span className="secure-label"><span>✓</span> Secure sign in</span>
          <span className="help-label">CAMPUS PORTAL</span>
        </div>

        <div className="login-content">
          <div className="mobile-brand">
            <span className="brand-icon">C</span>
            <span>Campus<strong>Connect</strong></span>
          </div>

          <div className="welcome-heading">
            <div className="welcome-icon">✧</div>
            <h2>Welcome back<span>.</span></h2>
            <p>Sign in to continue to your campus.</p>
          </div>

          <div className="role-selector" aria-label="Choose account type">
            {roles.map((item) => (
              <button
                type="button"
                key={item}
                className={`role-option ${role === item ? "active" : ""}`}
                onClick={() => { setRole(item); setMessage(""); }}
              >
                {item === "Student" ? "♧" : item === "Faculty" ? "▤" : "✦"}
                <span>{item}</span>
              </button>
            ))}
          </div>

          <form onSubmit={handleLogin}>
            <label htmlFor="email">Email address</label>
            <div className="input-wrap">
              <span className="input-icon">✉</span>
              <input
                id="email"
                type="email"
                placeholder="you@yourcollege.edu"
                autoComplete="username"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="password-label-row">
              <label htmlFor="password">Password</label>
              <span className="required-note">Required</span>
            </div>
            <div className="input-wrap">
              <span className="input-icon">▣</span>
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter your password"
                autoComplete="current-password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>

            <div className="form-options">
              <label className="remember-option">
                <input
                  type="checkbox"
                  checked={remember}
                  onChange={(e) => setRemember(e.target.checked)}
                />
                <span>Remember me</span>
              </label>
              <button type="button" className="text-button" onClick={() => setMessage("Please contact your campus administrator to reset your password.")}>
                Forgot password?
              </button>
            </div>

            {message && (
              <div className={`message ${message === "Login successful!" ? "success" : "error"}`} role="status">
                {message}
              </div>
            )}

            <button className="sign-in-button" type="submit" disabled={loading}>
              <span>{loading ? "Signing you in..." : "Sign in to CampusConnect"}</span>
              {!loading && <span className="button-arrow">→</span>}
            </button>
          </form>

          <div className="divider"><span>YOUR CAMPUS, CONNECTED</span></div>

          <div className="trust-row">
            <span>✓</span>
            <p>Your information is handled securely.</p>
          </div>
        </div>

        <footer className="login-footer">
          <span>© 2026 CampusConnect</span>
          <span>Made for campus life</span>
        </footer>
      </section>
    </main>
  );
}

export default App;
