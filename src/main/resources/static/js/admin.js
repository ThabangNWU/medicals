
var loader = document.querySelector(".loader")
function loading() {
    loader.style.display = 'none'
}

// PROFILE
const openNav = () => {
  document.getElementById("mySidenav").style.width = "350px";
}

const closeNav = () => {
  document.getElementById("mySidenav").style.width = "0";
}
// END

let sideBarBtn=document.querySelectorAll(".side-bar-list")
let divContainer=document.querySelectorAll(".is-active")

let btn1=   document.querySelector(".btn1")
let btn2=   document.querySelector(".btn2")
let btn3=   document.querySelector(".btn3")
let btn4=   document.querySelector(".btn4")
let btn5=   document.querySelector(".btn5")

btn1.addEventListener(("click"),()=>{
  switchBtn(0,0)

})

btn2.addEventListener(("click"),()=>{
  switchBtn(1,1)

})

btn3.addEventListener(("click"),()=>{
  switchBtn(2,2)

})

btn4.addEventListener(("click"),()=>{
  switchBtn(3,3)

})

btn5.addEventListener(("click"),()=>{
  switchBtn(4,4)

})

const switchBtn = (divSelected,btnSelected) => {

    sideBarBtn.forEach(element => {
        element.classList.remove("active")
       });
       sideBarBtn[btnSelected].classList.add("active")
       
       divContainer.forEach(element => {
         element.classList.remove("active")
        });
       
       divContainer[divSelected].classList.add("active")
}

const chartt = () => {

  var xValues = ["Product", "Product", "Product", "Product"];
  var yValues = [50, 40, 30, 20, 15, 5, 4, 3, 2, 1];
  var barColors = ["red", "green","blue","orange"];

  new Chart("myChart", {
    type: "bar",
    data: {
      labels: xValues,
      datasets: [{
        backgroundColor: barColors,
        data: yValues
      }]
    },
    options: {
      legend: {display: false},
      title: {
        display: true,
        text: "Thogwa Medicals Data For Year 2023"
      }
    }
  });
}

chartt()

var smll = document.querySelector('.small-msg')
var photo = document.getElementById('product-image').files[0]
const photoValid = () =>{
    smll.style.display = "block"
}

async function customers(){
  const responce = await fetch('/allCustomers');
  const data = await responce.json();
  console.log(data)

  for(cust in data){
    // console.log(cust.data())
  }

  const tble = document.querySelector('table');
  let html='';
  let div='';

  // if (data === '') {
  //     div = `
  //     <thead class="table-dark">
  //       <tr>
  //           <th>Name:</th>
  //           <th>Last Name:</th>
  //           <th>Email:</th>
  //           <th>Date Of Birth:</th>
  //           <th>Signed_On:</th>
  //           <th>Action:</th>
  //       </tr>
  //     </thead>
  //     <tbody>
  //             <tr>
  //                 <td>${data.Customer_Name}</td>
  //                 <td>${data.Customer_Surname}</td>
  //                 <td>${data.Customer_Email}</td>
  //                 <td>${data.Date_Of_Birth}</td>
  //                 <td>${data.Date_Registered}</td>
  //                 <td>
  //                     <a href="/delete/:${data.ID}" class="text-danger"><i class="fas fa-trash fa-lg"></i></a>
  //                 </td>
  //             </tr>
  //       </tbody>
  //   `
  //     html += div;
  //     tble.innerHTML = html;

  //   } else {
  //     tble.innerHTML = "No Customer registered yet!"
  //   }

}
customers()

// Logout
let log = document.querySelector(".logout")

log.addEventListener('click', () => {
  auth.signOut()
  .then(() => {
      //location.href = "admin-login.html"
  })
  .catch((error) => {
      alert(error)
      console.log(error)
  })
  
})