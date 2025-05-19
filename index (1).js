const express=require("express");
const app=express();

const cookieParser=require("cookie-parser");
const path=require("path");
const userRouter=require("./routes/userRouter")
const index=require("./routes/index")

app.set("view engine","ejs");
app.use(express.json())
app.use(express.urlencoded({extended:true}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname,"public")));

app.use("/",index);
app.use("/users",userRouter);

app.listen(3000)