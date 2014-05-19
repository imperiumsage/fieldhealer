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
        element.onmousedown = generateStartFunction(oldMouseDown, element);
        oldTouchStart = element.ontouchstart;
        element.ontouchstart = generateStartFunction(oldTouchStart, element);

        oldMouseUp = element.onmouseup;
        element.onmouseup = generateEndFunction(oldMouseUp, element);
        oldTouchEnd = element.ontouchend;
        element.ontouchend = generateEndFunction(oldTouchEnd, element);
    }

    function generateStartFunction(oldFunction, element) {
	return function() {
	    if (element.id.indexOf('primal') == 0) {
		new_element = element.cloneNode(true);
		element.parentNode.appendChild(new_element);
		element.parentNode.removeChild(element);
		document.getElementById('canvas').appendChild(element);
		jsPlumb.draggable(element.id, {containment: true});
		if (element.id.indexOf('state') != -1) {
		    element.id = 'state-' + globalState.stateId;
		    globalState.stateId++;
		} else {
		    element.id = 'condition-' + globalState.conditionId;
		    globalState.conditionId++;
		}
		jsPlumb.draggable(new_element);
		generateElementMouseEventHandler(new_element);
	    }
	    if (oldFunction != null) {
		oldFunction();
	    }
	}
    }
    function generateEndFunction(oldFunction, element) {
	return function() {
            if (element.getBoundingClientRect().left < document.getElementById("lhs").getBoundingClientRect().right) { 
	      element.parentNode.removeChild(element);
            } else {
		element.onmouseup = null;
		element.ontouchend = null;
	    }
	    if (oldFunction != null) {
		oldFunction();
	    }
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
