jsPlumb.bind("ready", function() {
  jsPlumb.draggable("primal-state");
  jsPlumb.draggable("primal-condition");
});

var globalState = {
    stateId: 1,
    conditionId: 1,
};

function addDragHandler(element) {
    function generateStartFunction(oldFunction, prefix) {
	return function() {
	    new_element = element.cloneNode(true);
	    element.parentNode.appendNode(new_element);
	    if (prefix == "state") {
		element.id = prefix + globalState.stateId;
		globalState.stateId++;
	    } else {
		element.id = prefix + globalState.conditionId;
		globalState.conditionId++;
	    }
	    oldFunction();
	}
    }
    oldMouseDown = element.onmousedown;
    element.onmousedown = generateStartFunction(oldMouseDown, "state");
    oldTouchStart = element.ontouchstart;
    element.ontouchstart = generateStartFunction(oldTouchStart, "condition");

    function generateEndFunction(oldFunction) {
	return function() {
	    element.parentNode.removeChild(element);
	    oldFunction();
	}
    }
    oldMouseUp = element.onmouseup;
    element.onmouseup = generateEndFunction(oldMouseUp);
    oldTouchEnd = element.ontouchend;
    element.ontouchend = generateEndFunction(oldTouchEnd);
}

function init() {
  primal_state = document.getElementById("primal-state")
  addDragHandler(primal_state)
  primal_condition = document.getElementById("primal-condition")
  addDragHandler(primal_condition)
}
