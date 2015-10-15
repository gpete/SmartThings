/**
 *  433 Outlet
 *
 *  Copyright 2015 Greg Peterson
 *
 */
metadata {
	definition (name: "433 Outlet", namespace: "gpete", author: "Greg Peterson") {
		capability "Switch"
        
        attribute "onCode", "string"
        attribute "offCode", "string"
	}

    tiles {
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
            state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
        }
        main "switch"
        details(["switch","on","off"])
    }
}

def init(Map attributes) {
	sendEvent(name: "onCode", value: attributes.code)
    sendEvent(name: "offCode", value: attributes.code2)
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
    if (device.onCode) {
        def result = parent.sendRFCommand(device.onCode)
        log.debug "Result: $result"
        sendEvent(name: "switch", value: "on")
    }
}

def off() {
    log.debug "Executing off()"
    if (device.offCode) {
        def result = parent.sendRFCommand(device.offCode)
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