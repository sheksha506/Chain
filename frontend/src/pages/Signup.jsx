import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'

const Signup = () => {

    const navigate = useNavigate();
    const[form, setForm] = useState({
        username:"",
        email:"",
        password:""
    })

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name] : e.target.value
        });
    }

   
    const UserDetailsSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/signup", form)
            console.log("Account successfully created");
            
            navigate("/login");
        }
        catch(error){
            console.log("error");
        }
    };
   

    return (
        <div className='h-screen'>


            <div className='flex flex-col items-center justify-center h-full'>
                <h1 className='text-4xl m-3 p-2 font-bold'>Signup</h1>

                <form onSubmit={UserDetailsSubmit} className="flex flex-col border p-6 bg-white rounded shadow-md w-full max-w-sm">
                    <input name="username"  onChange={handleChange} className='p-3 border my-1 rounded' type='text' placeholder='username' />
                    <input name="email"  onChange={handleChange} className='p-3 border my-1 rounded' type='email' placeholder='email' />
                    <input name="password"   onChange={handleChange} className='p-3 border my-1 rounded' type='password' placeholder='password' />
                    <button type='submit' className='bg-red-300 font-bold p-2 my-1 cursor-pointer'>Submit</button>
                </form>
            </div>
        </div>
    )
}

export default Signup