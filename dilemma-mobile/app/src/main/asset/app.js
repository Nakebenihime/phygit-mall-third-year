var floor = document.querySelector('#map');
var paths= floor.querySelectorAll('.image a');
var links = floor.querySelectorAll('.map_list a');



if(NodeList.prototype.forEach ===undefined){
    NodeList.prototype.forEach = function (callback){
        [].forEach.call(this,callback)
    }
}


paths.forEach(function(path){
    path.addEventListener('click',function(e){
        var id = this.id;
        console.log(id);
        activeArea(id);
    })
})

links.forEach(function(link){
    link.addEventListener('mouseenter',function(){
        var id = this.id.replace('list-','');
        activeArea(id);
    })
})

var activeArea = function (id){
    floor.querySelectorAll('.is-active').forEach(function(item){
        item.classList.remove('is-active');
    })

    if(id!==undefined){
        document.querySelector('#list-'+id).classList.add('is-active');
        document.querySelector('#'+id).classList.add('is-active');
    }

}

floor.addEventListener('mouseover',function(){
    activeArea();
})
