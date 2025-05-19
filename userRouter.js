const express=require("express");
const router=express.Router();
const userModel=require("../models/users-model");
const jwt=require("jsonwebtoken");
const bcrypt=require("bcrypt");

router.get("/",function(req,res){
    res.send("hey its working");
})
router.post("/register",async function(req,res){
    try{
        let{email,password,confirmpassword}=req.body;

        let user=await userModel.findOne({email:email});
        if(user) return res.status(401).send("You already have an account,please login")
        bcrypt.genSalt(10,function(err,salt){
            bcrypt.hash(password,salt,async function(err,hash){
                if(err) return res.send(err.message);
                else{
                    if(password===confirmpassword){
                        let user=await userModel.create({
                            email,
                            password:hash,
                            confirmpassword,
                        });
                        let token=jwt.sign({email,id:user._id},"heyheyhey");
                        res.cookie("token",token)
                        res.send("User Created Successfully");
                    }else{
                        res.send("Password did not matched");
                    }
                }
            })
        })
    }catch(err){
        console.log(err.message)
    }
})
router.post("/login",async function(req,res){
    try{
        let{email,password}=req.body;

        let user=await userModel.findOne({email:email});
        if(!user) return res.send("email or password incorrect");

        bcrypt.compare(password,user.password,function(err,result){
            if(result){
                let token=jwt.sign({email,id:user._id},"heyheyhey");
                res.cookie("token",token)
                res.send("User loggedin Successfully");
            }else{
                res.send("email or password incorrect");
            }
        })
    }catch(err){

    }
})
module.exports=router;