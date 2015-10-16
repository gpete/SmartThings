/**
 *  433 Outlet
 *
 *  Copyright 2015 Greg Peterson
 *
 */
metadata {
	definition (name: "433 Outlet", namespace: "gpete", author: "Greg Peterson") {
		capability "Switch"
	}

    tiles {
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "off", label: "Off", action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
            state "on", label: "On", action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
        }
        main "switch"
        details(["switch","on","off"])
    }
}

def init(Map attributes) {
	state.onCode = attributes.code
    state.offCode = attributes.code2
}

def rename(newName) {
	log.warn "Child device rename not yet implemented"
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def on() {
    log.debug "Executing on()"
    if (state.onCode) {
        def result = parent.sendRFCommand(state.onCode)
        log.debug "Result: $result"
        sendEvent(name: "switch", value: "on")
    }
}

def off() {
    log.debug "Executing off()"
    if (state.offCode) {
        def result = parent.sendRFCommand(state.offCode)
        log.debug "Result: $result"
        sendEvent(name: "switch", value: "off")
    }
}

def sendOnEvent() {
	log.debug "Sending on event"
    sendEvent(name: "switch", value: "on")
}

def sendOffEvent() {
	log.debug "Sending off event"
    sendEvent(name: "switch", value: "off")
}

def setOnCode(newOnCode) {
	state.onCode = newOnCode
}

def getOnCode() {
	return state.onCode
}

def setOffCode(newOffCode) {
	state.offCode = newOffCode
}

def getOffCode() {
	return state.offCode
}