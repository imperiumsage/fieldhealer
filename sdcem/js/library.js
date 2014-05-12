jsPlumb.bind("ready", function() {
  jsPlumb.draggable("primal-state");
  jsPlumb.draggable("primal-condition");
});

var globalState = {
    stateId: 1,
    conditionId: 1,
};

function addDragHandler(element) {
    function generateElementMouseEventHandler(element) {
        oldMouseDown = element.onmousedown;
        element.onmousedown = generateStartFunction(oldMouseDown, "state", element);
        oldTouchStart = element.ontouchstart;
        element.ontouchstart = generateStartFunction(oldTouchStart, "condition", element);

        oldMouseUp = element.onmouseup;
        element.onmouseup = generateEndFunction(oldMouseUp, element);
        oldTouchEnd = element.ontouchend;
        element.ontouchend = generateEndFunction(oldTouchEnd, element);
    }

    function resurrectElementMouseEventHandler(element) {
        element.onmousedown = null;
        element.ontouchstart = null;
        element.onmouseup = null;
        element.ontouchend = null;
    }

    function generateStartFunction(oldFunction, prefix, element) {
	return function() {
	    new_element = element.cloneNode(true);
	    element.parentNode.appendChild(new_element);
	    if (prefix == "state") {
		element.id = prefix + globalState.stateId;
		globalState.stateId++;
	    } else {
		element.id = prefix + globalState.conditionId;
		globalState.conditionId++;
	    }
            jsPlumb.draggable(new_element);
            generateElementMouseEventHandler(new_element);
            resurrectElementMouseEventHandler(element);
	    oldFunction();
	}
    }
    function generateEndFunction(oldFunction, element) {
	return function() {
            if (element.getBoundingClientRect().left() < document.getElementById("lhs").getBoundingClientRect().right()) { 
	      element.parentNode.removeChild(element);
            }
	    oldFunction();
	}
    }
    generateElementMouseEventHandler(element);
}

function init() {
  primal_state = document.getElementById("primal-state")
  addDragHandler(primal_state)
  primal_condition = document.getElementById("primal-condition")
  addDragHandler(primal_condition)
}
