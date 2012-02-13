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
	callAjax("get", initialize, {});
}

// Once getting the list, append it ot the "menu"
// Also, remove the dummy childe "hidden"
// when all the items are appended, pulsate
function initialize(ajax){
//	loadingDone();
	$("foods").removeChild($("hidden"));
	var food_list = ajax.responseText.split("\n");

	for(var i = 0; i < food_list.length; i++){
		if(food_list[i] != ""){	
			var each_food = $(document.createElement("li"));
			each_food.className = "busy";
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

// This is for adding a new menu.
// Assign id.
// Append a new item to the end of the list. 
// Then, using Ajax, save its change.
function add(event){
	var lastAdded = $$("div.added_food");
	var len = lastAdded.length;
	if(len > 0){
		$("capture").removeChild(lastAdded[len - 1]);
	}
	var newItem = this.value; 
	
	if(newItem != ""){		
		var newfood = $(document.createElement("div"));
		newfood.id = "added_menu";
		newfood.className = "added_food";
		newfood.innerHTML = newItem + " has been added";
		$("capture").appendChild(newfood);
		
		callAjax("post", effectAdd, {"action": "add", "item": newItem});
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
	$("foods").shake();
}

// This function for changing order.
// Get a new string for the whole list,
// then, contact to server to reflect this change.
function listUpdate(list) {
//	list.shake();
	var newSet = $$("li.busy");
	var newString = "";
	for(var i = 0; i < newSet.length; i++){
		newString = newString + newSet[i].innerHTML + "\n";
	}
	callAjax("post", afterUpdate, {"action": "set", "items": newString});
}

// After done, pulsate the whole list.
function afterUpdate(){
//	loadingDone();
	$("foods").pulsate();	
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
