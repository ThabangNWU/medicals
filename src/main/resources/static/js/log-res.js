
//SWITCH LINKS

let log=document.getElementById("log_in")//x
let res=document.getElementById("register")//
let reset=document.getElementById("reset-pass")//

document.querySelector('.register-button').addEventListener('click', ()=>{
  log.style.left="-530px";
  res.style.left="0";
  reset.style.left = "-1060px"
})
document.querySelector('.forgot-password').addEventListener('click', ()=>{
  reset.style.left = "0"
  log.style.left="530px";
  res.style.left="1060px";
})

const Login_switch = ()=>{
  log.style.left="0";
  res.style.left="530px";
  reset.style.left="-530px";
}
// SWITCH END







var myInput = document.getElementById("psw");
var letter = document.getElementById("letter");
var capital = document.getElementById("capital");
var number = document.getElementById("number");
var length = document.getElementById("length");
var myInput2=document.getElementById("confirmPassword");

// When the user clicks on the password field, show the message box
//myInput.onfocus = function() {
//  document.getElementById("message").style.display = "block";
//}
//myInput2.onfocus = function() {
//  document.getElementById("message").style.display = "block";
//}

// When the user clicks outside of the password field, hide the message box
//myInput.onblur = function() {
//  document.getElementById("message").style.display = "none";
//}

function Check() {
    let checkAns=document.getElementById("check");
    if (checkAns.checked == true){


    } else {

        alert("Please Accept Terms and Conditions");
        return false;
    }

}
