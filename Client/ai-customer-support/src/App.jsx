import "bootstrap/dist/css/bootstrap.min.css";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import {
  Route,
  RouterProvider,
  createBrowserRouter,
  createRoutesFromElements,
} from "react-router-dom";
import RootLayout from "./components/layout/RootLayout.jsx";
import Home from "./components/home/Home.jsx";
import Chat from "./components/chat/Chat.jsx";
import AdminDashboard from "./components/admin/AdminDashboard.jsx";
import Ticket from "./components/ticket/Ticket.jsx";

function App() {
  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/" element={<RootLayout />}>
        <Route index element={<Home />} />
        <Route path="/chat" element={<Chat />} />
        <Route path="/tickets" element={<AdminDashboard />} />
        <Route path="/tickets/:ticketId" element={<Ticket />} />
      </Route>,
    ),
  );
  return <RouterProvider router={router} />;
}

export default App;
