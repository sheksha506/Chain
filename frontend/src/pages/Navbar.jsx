import React from 'react'
import { useNavigate } from 'react-router-dom'

const Navbar = () => {
    const navigate = useNavigate();
  return (
    <div className='bg-green-300 flex items-center justify-between p-3'>
        <div className='items-center space-x-2'>
            
            <button className='font-bold cursor-pointer'>CHAINN</button>
        </div>
        <div className=' mr-3 space-x-2'>
            <button onClick={() => navigate("/signup")} className='p-2 bg-red-600 text-white cursor-pointer'>Signup</button>
            <button className='p-2 cursor-pointer'>Login</button>
        </div>
    </div>
  )
}

export default Navbar