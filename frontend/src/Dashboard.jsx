
import { useEffect, useState } from "react";
import "./Dashboard.css";

const API = "https://campusconnect-2-souu.onrender.com";
export default function Dashboard({ user, onLogout }) {
  const [stats, setStats] = useState(null);
  const [notices, setNotices] = useState([]);
  const [events, setEvents] = useState([]);
  const [activePage, setActivePage] = useState("Overview");
  const [error, setError] = useState("");
  const [users, setUsers] = useState([]);
const [usersError, setUsersError] = useState("");

async function loadUsers() {
  const token =
    localStorage.getItem("campusconnect_token") ||
    sessionStorage.getItem("campusconnect_token");

  try {
    setUsersError("");
    const response = await fetch(`${API}/api/users`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error("Could not load users. Please sign in again.");
    }

    setUsers(await response.json());
  } catch (e) {
    setUsersError(e.message);
  }
}

  useEffect(() => {
    const token =
      localStorage.getItem("campusconnect_token") ||
      sessionStorage.getItem("campusconnect_token");

    if (!token) {
      setError("Please sign in again to continue.");
      return;
    }

    const headers = { Authorization: `Bearer ${token}` };

    async function loadDashboard() {
      try {
        if (user.role === "ADMIN") {
          const response = await fetch(`${API}/api/admin/dashboard`, { headers });
          if (!response.ok) throw new Error("Could not load admin statistics.");
          setStats(await response.json());
        }

        const [noticeResponse, eventResponse] = await Promise.all([
          fetch(`${API}/api/notices`, { headers }),
          fetch(`${API}/api/events`, { headers }),
        ]);

        if (noticeResponse.ok) setNotices(await noticeResponse.json());
        if (eventResponse.ok) setEvents(await eventResponse.json());
      } catch (e) {
        setError(e.message || "Unable to load dashboard data.");
      }
    }

    loadDashboard();
  }, [user.role]);
  useEffect(() => {
  if (user.role === "ADMIN") {
    loadUsers();
  }
}, [user.role]);

  const navigation = [
    { name: "Overview", icon: "◫" },
    { name: "Notices", icon: "▤" },
    { name: "Events", icon: "▦" },
    { name: "Subjects", icon: "▣" },
    { name: "Attendance", icon: "◷" },
    { name: "Discussions", icon: "◎" },
  ];

  if (user.role === "ADMIN") {
    navigation.push({ name: "User Management", icon: "♙" });
  }

  const totalStats = user.role === "ADMIN" && stats
    ? [
        { label: "Total users", value: stats.totalUsers, icon: "♙", color: "blue" },
        { label: "Students", value: stats.totalStudents, icon: "♧", color: "purple" },
        { label: "Faculty", value: stats.totalFaculty, icon: "▤", color: "green" },
        { label: "Campus events", value: stats.totalEvents, icon: "▦", color: "orange" },
      ]
    : [
        { label: "Notices", value: notices.length, icon: "▤", color: "blue" },
        { label: "Upcoming events", value: events.length, icon: "▦", color: "purple" },
      ];

  return (
    <div className="dashboard-layout">
      <aside className="sidebar">
        <a className="dashboard-brand" href="#overview">
          <span className="dashboard-logo">C</span>
          <span>Campus<strong>Connect</strong></span>
        </a>

        <p className="nav-caption">WORKSPACE</p>
        <nav className="side-nav">
          {navigation.map((item) => (
            <button
              key={item.name}
              className={`nav-item ${activePage === item.name ? "selected" : ""}`}
              onClick={() => setActivePage(item.name)}
            >
              <span className="nav-icon">{item.icon}</span>
              {item.name}
              {activePage === item.name && <span className="nav-indicator" />}
            </button>
          ))}
        </nav>

        <div className="sidebar-bottom">
          <div className="help-card">
            <span className="help-icon">✦</span>
            <strong>CampusConnect</strong>
            <p>Your campus, all in one place.</p>
          </div>
          <button className="logout-action" onClick={onLogout}>
            <span>↪</span> Sign out
          </button>
        </div>
      </aside>

      <main className="dashboard-main">
        <header className="dashboard-header">
          <div>
            <span className="header-eyebrow">CAMPUS WORKSPACE</span>
            <h1>{activePage}</h1>
          </div>
          <div className="profile-chip">
            <div className="profile-avatar">{user.name?.charAt(0)?.toUpperCase() || "U"}</div>
            <div>
              <strong>{user.name}</strong>
              <span>{user.role}</span>
            </div>
          </div>
        </header>

        {error && <div className="dashboard-error">{error}</div>}

        {activePage === "Overview" && (
          <>
            <section className="dashboard-hero">
              <div>
                <span className="hero-tag">✦ YOUR CAMPUS, CONNECTED</span>
                <h2>Welcome back, {user.name?.split(" ")[0] || "there"}!</h2>
                <p>Here's what's happening in your campus today.</p>
              </div>
              <div className="hero-art" aria-hidden="true">
                <div className="hero-orbit orbit-a" />
                <div className="hero-orbit orbit-b" />
                <span>✦</span>
              </div>
            </section>

            <section className="stats-grid">
              {totalStats.map((item) => (
                <article className="dashboard-stat" key={item.label}>
                  <div className={`stat-icon ${item.color}`}>{item.icon}</div>
                  <p>{item.label}</p>
                  <strong>{item.value ?? "—"}</strong>
                  <span className="stat-note">Live campus data</span>
                </article>
              ))}
            </section>

            <section className="dashboard-columns">
              <article className="content-card">
                <div className="card-heading">
                  <div><h3>Latest notices</h3><p>Important campus announcements</p></div>
                  <button onClick={() => setActivePage("Notices")}>View all →</button>
                </div>
                {notices.length === 0 ? (
                  <div className="empty-state"><span>▤</span><strong>No notices yet</strong><p>Campus announcements will appear here.</p></div>
                ) : notices.slice(0, 4).map((notice) => (
                  <div className="notice-row" key={notice.id}>
                    <span className="notice-mark">N</span>
                    <div><strong>{notice.title}</strong><p>{notice.description}</p></div>
                  </div>
                ))}
              </article>

              <article className="content-card">
                <div className="card-heading">
                  <div><h3>Campus events</h3><p>What's coming up</p></div>
                  <button onClick={() => setActivePage("Events")}>View all →</button>
                </div>
                {events.length === 0 ? (
                  <div className="empty-state"><span>▦</span><strong>No events yet</strong><p>New campus events will appear here.</p></div>
                ) : events.slice(0, 4).map((event) => (
                  <div className="event-row" key={event.id}>
                    <span className="event-mark">✦</span>
                    <div><strong>{event.name}</strong><p>{event.category || "Campus event"} · {event.eventDate || "Date to be announced"}</p></div>
                  </div>
                ))}
              </article>
            </section>
          </>
        )}

        {activePage === "Notices" && (
          <section className="content-card full-card">
            <div className="card-heading"><div><h3>Campus notices</h3><p>Announcements from your campus</p></div></div>
            {notices.length === 0 ? <div className="empty-state"><span>▤</span><strong>No notices available</strong><p>Check back later for updates.</p></div> : notices.map((notice) => (
              <div className="notice-row" key={notice.id}><span className="notice-mark">N</span><div><strong>{notice.title}</strong><p>{notice.description}</p><small>{notice.createdBy || "Campus administration"}</small></div></div>
            ))}
          </section>
        )}

        {activePage === "Events" && (
          <section className="content-card full-card">
            <div className="card-heading"><div><h3>Campus events</h3><p>Explore campus activities</p></div></div>
            {events.length === 0 ? <div className="empty-state"><span>▦</span><strong>No events available</strong><p>New events will show here when published.</p></div> : events.map((event) => (
              <div className="event-row" key={event.id}><span className="event-mark">✦</span><div><strong>{event.name}</strong><p>{event.description}</p><small>{event.category} · {event.eventDate}</small></div></div>
            ))}
          </section>
        )}

       {activePage === "User Management" && (
  <section className="content-card full-card">
    <div className="card-heading">
      <div>
        <h3>Registered users</h3>
        <p>Users registered in CampusConnect</p>
      </div>
    </div>

    <button onClick={loadUsers}>Refresh users</button>

    {usersError && <p>{usersError}</p>}

    {users.length === 0 && !usersError ? (
      <p>No users found.</p>
    ) : (
      <div>
        {users.map((item) => (
          <div className="notice-row" key={item.id}>
            <div>
              <strong>{item.name}</strong>
              <p>{item.email}</p>
              <small>{item.role} · {item.department || "No department"}</small>
            </div>
          </div>
        ))}
      </div>
    )}
  </section>
)}

{!["Overview", "Notices", "Events", "User Management"].includes(activePage) && (
  <section className="content-card full-card">
    <div className="empty-state">
      <span>✧</span>
      <strong>{activePage}</strong>
      <p>This section will be connected in the next step.</p>
    </div>
  </section>
)}
        <footer className="dashboard-footer">
          <span>© 2026 CampusConnect</span>
          <span>Designed for a smarter campus ✦</span>
        </footer>
      </main>
    </div>
  );
}
