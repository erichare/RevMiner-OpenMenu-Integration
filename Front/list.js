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
	$("add_new_food").observe("click", direct_add_food);
	$("add_new_rest").observe("click", direct_add_rest);
	$("delete_food").observe("click", beforeDelete_food);	
	$("deleteAll_food").observe("click", beforeDeleteAll_food);	
	$("delete_rest").observe("click", beforeDelete_rest);	
	$("deleteAll_rest").observe("click", beforeDeleteAll_rest);	
	fetchNode_food();
	fetchNode_rest();
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
function fetchNode_food(){
	callAjax("get", initialize_food, {"action": "get_food"});
}

function fetchNode_rest(){
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
			
			var food_link = $(document.createElement("a"));
			food_link.href="match.php?want=Food&menu_rest_name=" + food_list[i];
			food_link.innerHTML = food_list[i];
			each_food.appendChild(food_link);
			
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
			
			var rest_link = $(document.createElement("a"));
			rest_link.href = "match.php?want=Restaurant&menu_rest_name=" + food_list[i];
			rest_link.innerHTML = food_list[i];
			each_food.appendChild(rest_link);
			
			$("rests").appendChild(each_food);
		}
	}

	Sortable.create("rests", {
		onUpdate: listUpdate
	});
}

function add_helper(event){
	var lastAdded = $$("div.added_food");
	var len = lastAdded.length;
	if(len > 0){
		$("capture").removeChild(lastAdded[len - 1]);
	}
}

// This is for adding a new menu/restaurant.
// Assign id.
// Append a new item to the end of the list. 
// Then, using Ajax, save its change.
function add(event){
	add_helper(this);
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

// When a user types a menu name and add it.
function direct_add_food(event){
	
	add_helper(this);
	
	var newItem = $("new_food").value;
	if(newItem != ""){		
		var newFood = $(document.createElement("li"));
		newFood.className = "busy1";
		newFood.id = "direct_food_" + ID;
		ID++;
		
		var food_link = $(document.createElement("a"));
		food_link.href="match.php?want=Food&menu_rest_name=" + newItem;
		food_link.innerHTML = newItem;
		newFood.appendChild(food_link);
		
		$("foods").appendChild(newFood);
		Sortable.create("foods", {
			onUpdate: listUpdate
		});
		
		callAjax("post", effectAdd, {"action": "add_food", "item": newItem});
	}
}

// When a user types a restaurant name and add it
function direct_add_rest(event){
	add_helper(this);
	var newItem = $("new_rest").value;
	if(newItem != ""){		
		var newRest = $(document.createElement("li"));
		newRest.className = "busy2";
		newRest.id = "direct_rest_" + ID;
		ID++;
		
		var rest_link = $(document.createElement("a"));
		rest_link.href="match.php?want=Restaurant&menu_rest_name=" + newItem;
		rest_link.innerHTML = newItem;
		newRest.appendChild(rest_link);
		
		$("rests").appendChild(newRest);
		Sortable.create("rests", {
			onUpdate: listUpdate
		});
		callAjax("post", effectAdd, {"action": "add_rest", "item": newItem});
	}
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
function beforeDelete_food(){
	var last = $$("li.busy1");
	
	if(last.length != 0){
		$(last[0]).fade({
			afterFinish: deleteReally_food	
		});
	}
}

// Before delete, set afterFinish effect.
function beforeDelete_rest(){
	var last = $$("li.busy2");
	
	if(last.length != 0){
		$(last[0]).fade({
			afterFinish: deleteReally_rest
		});
	}
}

// Before delete, set afterFinish effect.
function beforeDeleteAll_food(){
	var last = $$("li.busy1");
	var i = 0;
	
	for(i = 0; i < last.length; i++){
		$(last[i]).fade({
			afterFinish: deleteReally_food
		});
	}
}

// Before delete, set afterFinish effect.
function beforeDeleteAll_rest(){
	var last = $$("li.busy2");
	var i = 0;
	
	for(i = 0; i < last.length; i++){
		$(last[i]).fade({
			afterFinish: deleteReally_rest
		});
	}
}

// Delete the first item of the list, and then ask server to delete.
function deleteReally_food(effect){
	var last = $$("li.busy1");
	if(last.length != 0){ 
		$("foods").removeChild(last[0]);
		Sortable.create("foods", {
			onUpdate: listUpdate
		});
		
		callAjax("post", afterDelete, {"action": "delete_food"});
	}
}

// Delete the first item of the list, and then ask server to delete.
function deleteReally_rest(effect){
	var last = $$("li.busy2");
	if(last.length != 0){ 
		$("rests").removeChild(last[0]);
		Sortable.create("rests", {
			onUpdate: listUpdate
		});
		
		callAjax("post", afterDelete, {"action": "delete_rest"});
	}
}

// After done deleting, shaking it.
function afterDelete(){
//	loadingDone();
//	$("foods").pulsate();
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
//	$("foods").pulsate();	
//	$("rests").pulsate();	
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
