
//Button menu
let menu_btn = document.getElementById("main_menu");
let menu_links = document.getElementById("links_menu");
let cont = true;

menu_btn.addEventListener("click", function(){
    if (cont){
        menu_links.className = ("final_menu");
        cont=false;
    }else{
        menu_links.classList.remove("final_menu");
        menu_links.className = ("inicial_menu");
        cont=true;
    }
})

//Animate svg text
const svg = document.querySelectorAll("#svg_text path");
console.log(svg);
for (let i = 0; i < svg.length; i++){
    console.log(`Letter ${i} is ${svg[i].getTotalLenght()}`);
}