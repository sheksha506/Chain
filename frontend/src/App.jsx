import { Route, Routes } from "react-router-dom"
import Navbar from "./pages/Navbar"
import Signup from "./pages/Signup"
import Login from "./pages/Login"
import Main from "./pages/Main"
import ProtectRoute from "./components/ProtectRoute"


function App() {
  

  return (
    <>
     <div>
      <Routes>
        <Route path='/' element={<Navbar/>} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/main" element={<ProtectRoute><Main /></ProtectRoute>} />
      </Routes>
      
     </div>
    </>
  )
}

export default App
