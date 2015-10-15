/**
 *  433 Motion
 *
 *  Copyright 2015 Greg Peterson
 *
 */
metadata {
	definition (name: "433 Motion", namespace: "gpete", author: "Greg Peterson") {
		capability "Motion Sensor"
	}

	simulator {
		status "active": "active"
		status "inactive": "inactive"
	}

	tiles {
		standardTile("motion", "device.motion", width: 2, height: 2) {
			state("active", label:'motion', icon:"st.motion.motion.active", backgroundColor:"#53a7c0")
			state("inactive", label:'no motion', icon:"st.motion.motion.inactive", backgroundColor:"#ffffff")
		}

		main "motion"
		details (["motion"])
	}
}

def init(Map attributes) {
	device.inactivate()
}

def rename(newName) {
	log.warn "Child device rename not yet implemented"
}

// parse events into attributes
def parse(String event) {
	log.debug "Parsing '${event}'"
    def result
    if (event.equals("updated")) {
    	result = createEvent(name: "motion", value: "inactive")
    }

	log.debug "Parse returned ${result?.descriptionText}"
	return result
}

def activate() {
	log.debug "activate()"
    sendEvent(name: "motion", value: "active")
}

def inactivate() {
    log.debug "inactivate()"
    sendEvent(name: "motion", value: "inactive")
}