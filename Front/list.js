/*
author: Donghyo Min

filename: list.js
This file is for javascript for index.php
*/

// This global variable to make an ID for each item unique.
// Even though delete is happening, an added new item after deleting
// is going to be assigned with this ID, so each item would get a 
// unique. 
// This one should be gloable since I would use it when I initaialize the page
// using the list already saved in list.txt and also when I add a new item.
// This value would be increased in only two cases: 
// First, when I loaded from the list.txt initially, Second, when I add a new item.
var ID = 0;

// After the page is done loading, call functions and handlers run.
document.observe('dom:loaded', function () {
//	loadingIMG();
//	$("add").observe("click", add);
	$("delete").observe("click", beforeDelete);	
	$("deleteAll").observe("click", beforeDeleteAll);	
	fetchNode();
});

/*
// This function is for "Loading..." image to indicate the page is waiting.
// The image is going to be a child of the "user"
// Initially, hide this image.
function loadingIMG(){
	var image = $(document.createElement("img"));
	image.src = "http://students.washington.edu/dongm/454/img/loading.gif";
	image.alt = "loading animation";
	image.id = "loading";
	$("user").appendChild(image);
	$("loading").hide();
}

// While loading, Loading image appears, 
// set cursor to hourglass.
function loadingStart(){
	$("loading").show();
	document.body.style.cursor = "wait";
}

// Once waiting done, hide image.
// turn off hourglass cursor. 
function loadingDone(){
	$("loading").hide();
	document.body.style.cursor = "default";		
}
*/
// This one is helper function whenever the webpage communicate with server.
// param methodCall: either "get" or "post"
// param afterFunc: define which function would be executed on success.
// param param: parameters if needed.
function callAjax(methodCall, afterFunc, param){
//	loadingStart();
	new Ajax.Request("http://www.project-fin.org/openmenu/Front/list_save.php",
		{
			method: methodCall,
			onSuccess: afterFunc,
			parameters: param,
			onFailure: ajaxFailure,
			onException: ajaxFailure
		}
	);
}

// Communicate to server to get the saved list.
function fetchNode(){
	callAjax("get", initialize_food, {"action": "get_food"});
	callAjax("get", initialize_rest, {"action": "get_rest"});
}

// Once getting the list, append it ot the "foods"
// Also, remove the dummy childe "hidden"
// when all the items are appended, pulsate
function initialize_food(ajax){
//	loadingDone();
	$("foods").removeChild($("hidden1"));
	var food_list = ajax.responseText.split("\n");

	for(var i = 0; i < food_list.length; i++){
		if(food_list[i] != ""){	
			var each_food = $(document.createElement("li"));
			each_food.className = "busy1";
			each_food.id = "foods_" + ID;
			ID++;
			each_food.innerHTML = food_list[i];
			$("foods").appendChild(each_food);
		}
	}

	Sortable.create("foods", {
		onUpdate: listUpdate
	});
}

// Once getting the list, append it ot the "rests"
// Also, remove the dummy childe "hidden"
// when all the items are appended, pulsate
function initialize_rest(ajax){
//	loadingDone();
	$("rests").removeChild($("hidden2"));
	var food_list = ajax.responseText.split("\n");

	for(var i = 0; i < food_list.length; i++){
		if(food_list[i] != ""){	
			var each_food = $(document.createElement("li"));
			each_food.className = "busy2";
			each_food.id = "rests_" + ID;
			ID++;
			each_food.innerHTML = food_list[i];
			$("rests").appendChild(each_food);
		}
	}

	Sortable.create("rests", {
		onUpdate: listUpdate
	});
}

// This is for adding a new menu/restaurant.
// Assign id.
// Append a new item to the end of the list. 
// Then, using Ajax, save its change.
function add(event){
	var lastAdded = $$("div.added_food");
	var len = lastAdded.length;
	if(len > 0){
		$("capture").removeChild(lastAdded[len - 1]);
	}
	var newItem = event.value; 
	
	if(newItem != ""){		
		var newfood = $(document.createElement("div"));
		newfood.id = "added_menu";
		newfood.className = "added_food";
		newfood.innerHTML = newItem + " has been added";
		$("capture").appendChild(newfood);
	}

	return newItem;
}

// add a new restaurant on the list
function add_rest(event){
	var newItem = add(this);
	
	if(newItem != ""){		
		callAjax("post", effectAdd, {"action": "add_rest", "item": newItem});
	}
}

// add a new food on the list
function add_food(event){
	var newItem = add(this);

	if(newItem != ""){		
		callAjax("post", effectAdd, {"action": "add_food", "item": newItem});
	}
}

// fade out a message of the new item.
function effectAdd(){
	$("added_menu").fade({ 
		duration: 3.5
	});
}

// Before delete, set afterFinish effect.
function beforeDelete(){
	var last = $$("li.busy");
	if(last.length != 0){
		$(last[0]).fade({
			afterFinish: deleteReally	
		});
	}
}

// Before delete, set afterFinish effect.
function beforeDeleteAll(){
	var last = $$("li.busy");
	var i = 0;
	
	for(i = 0; i < last.length; i++){
		$(last[i]).fade({
			afterFinish: deleteReally	
		});
	}
}

// Delete the first item of the list, and then ask server to delete.
function deleteReally(effect){
	var last = $$("li.busy");
	if(last.length != 0){ 
		$("foods").removeChild(last[0]);
		Sortable.create("foods", {
			onUpdate: listUpdate
		});
		
		callAjax("post", afterDelete, {"action": "delete"});
	}
}

// After done deleting, shaking it.
function afterDelete(){
//	loadingDone();
	$("foods").pulsate();
}

// This function for changing order.
// Get a new string for the whole list,
// then, contact to server to reflect this change.
function listUpdate(list) {
//	list.shake();
	var newSet1 = $$("li.busy1");
	var newString1 = "";
	for(var i = 0; i < newSet1.length; i++){
		newString1 = newString1 + newSet1[i].innerHTML + "\n";
	}
	callAjax("post", afterUpdate, {"action": "set_food", "items": newString1});
	
	var newSet2 = $$("li.busy2");
	var newString2 = "";
	for(var i = 0; i < newSet2.length; i++){
		newString2 = newString2 + newSet2[i].innerHTML + "\n";
	}
	callAjax("post", afterUpdate, {"action": "set_rest", "items": newString2});
}

// After done, pulsate the whole list.
function afterUpdate(){
//	loadingDone();
	$("foods").pulsate();	
	$("rests").pulsate();	
}

// This is for failure function.
// show some appropiate message.
// After dealing with error, the page goes back to be available.
function ajaxFailure(ajax, exception) {
//	loadingDone();
	var err = $(document.createElement("div"));
	err.addClassName("mistake");
	err.id = "errParent";
	$("errors").appendChild(err);

	var err1 = $(document.createElement("p"));
	var err2 = $(document.createElement("p"));
	
	err1.innerHTML = "ERROR: Server status: " + ajax.status + " " + ajax.statusText;
	err2.innerHTML = "ERROR: Server response text: " + ajax.responseText;	
	
	$("errParent").appendChild(err1);
	$("errParent").appendChild(err2);

	if (exception) {
		throw exception;
	}
}
