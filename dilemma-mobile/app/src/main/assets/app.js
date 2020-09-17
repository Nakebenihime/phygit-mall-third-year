
var map = document.querySelector('#map_svg');
var paths= map.querySelectorAll('.image_map a');
var nodeList = [8,15,16];


window.onload = function() {
       //updateFromAndroid([15,50,55,51]);
       //updateFromAndroid(['Alley_15','Alley_56','Alley_55','Alley_51']);
}

function updateFromAndroid(val){
        //console.log(val + "                   MMMMMMMMMMMMMMm");
       for(let j of val){
       console.log( j+ "            valeur");
        document.getElementById(j).classList.add("journey");
       }


}

paths.forEach(function(path){
    path.addEventListener('click',function(e){
        var id = this.id;
        console.log(id + "  ID");
       console.log(e);
    })
})




