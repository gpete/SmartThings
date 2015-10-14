/**
 *  Photon RF Gateway
 *
 *  Copyright 2015 Greg Peterson
 *
 *
 */

definition(
    name: "Photon RF Gateway",
    namespace: "gpete",
    author: "Greg Peterson",
    description: "Adds 433MHz RF capability to SmartThings.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {    
	page(name: "mainPage")
    page(name: "manageDevicesPage")
    page(name: "loginPage")
}


mappings {
    path("/rf433") {
        action: [
            POST: "parseIncomingData"
        ]
    }
}

def mainPage() {
	state.particleAPIUri = "https://api.particle.io/v1"
    state.particleEventName = "rf433"
    state.particleSendCommandFunction = "rf433"
	state.supportedCapabilities = ["Motion Sensor"]//,"Button","Contact Sensor"]
    state.maxNumDevices = 10
    populateSmartThingsAccessToken()
    
	dynamicPage(name: "mainPage", nextPage: null, uninstall: true, install: true) {      
        section("Photon") {
        	href(name: "particleLogin", page: "loginPage", title: "Photon", description: "")
        }
        
    	section("Devices") {
        	href(name: "manageDevices", page: "manageDevicesPage", title: "Manage Devices", description: "Add or edit devices...")
        }
        
        section("Device Settings") {
        	input(name: "inactiveCheckTime", title: "Device Activity Timeout (seconds)", type: "number", required: true)
        }
    }
}

def manageDevicesPage() {
	dynamicPage(name: "manageDevicesPage", nextPage: null, uninstall: false, install: false) {
    	def supportedCapabilities = ["Button","Contact Sensor","Motion Sensor"]
        
//		  Can't use this due to the Android app not working with the inputs with dynamic names
//        for (int i = 1; i <= state.maxNumDevices; i++) {
//            section("Device ${i}") {
//                input(name: "device${i}Name", title: "Device name", type: "text", required: false)
//                input(name: "device${i}Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
//                input(name: "device${i}Code", title: "Device code", type: "text", required: false)
//            }
//        }

        section("Device 1") {
            input(name: "device1Name", title: "Device name", type: "text", required: false)
            input(name: "device1Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device1Code", title: "Device code", type: "text", required: false)
        }
        section("Device 2") {
            input(name: "device2Name", title: "Device name", type: "text", required: false)
            input(name: "device2Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device2Code", title: "Device code", type: "text", required: false)
        }
        section("Device 3") {
            input(name: "device3Name", title: "Device name", type: "text", required: false)
            input(name: "device3Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device3Code", title: "Device code", type: "text", required: false)
        }
        section("Device 4") {
            input(name: "device4Name", title: "Device name", type: "text", required: false)
            input(name: "device4Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device4Code", title: "Device code", type: "text", required: false)
        }
        section("Device 5") {
            input(name: "device5Name", title: "Device name", type: "text", required: false)
            input(name: "device5Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device5Code", title: "Device code", type: "text", required: false)
        }
        section("Device 6") {
            input(name: "device6Name", title: "Device name", type: "text", required: false)
            input(name: "device6Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device6Code", title: "Device code", type: "text", required: false)
        }
        section("Device 7") {
            input(name: "device7Name", title: "Device name", type: "text", required: false)
            input(name: "device7Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device7Code", title: "Device code", type: "text", required: false)
        }
        section("Device 8") {
            input(name: "device8Name", title: "Device name", type: "text", required: false)
            input(name: "device8Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device8Code", title: "Device code", type: "text", required: false)
        }
        section("Device 9") {
            input(name: "device9Name", title: "Device name", type: "text", required: false)
            input(name: "device9Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device9Code", title: "Device code", type: "text", required: false)
        }
        section("Device 10") {
            input(name: "device10Name", title: "Device name", type: "text", required: false)
            input(name: "device10Type", title: "Device type", type: "enum", required: false, options: supportedCapabilities)
            input(name: "device10Code", title: "Device code", type: "text", required: false)
        }
    }
}

def loginPage() {
	dynamicPage(name: "loginPage") {
    	if (settings.particleUsername && settings.particlePassword && !state.particleToken) {
        	populateParticleToken()
        }
        
    	if (!state.particleToken) {
            section("Please sign into your Particle account") {
                input(name: "particleUsername", type: "text", title: "Particle Username", required: true)
                input(name: "particlePassword", type: "password", title: "Particle Password", required: true, submitOnChange: true)
            }
        }
        else {
        	section("Select Device") {
            	def particleDevices = getParticleDevices()
                input(name: "particleDevice", type: "enum", title: "Select a device", required: true, multiple: false, options: particleDevices)
            }
        }
    }
}

def getParticleDevices() {
	def particleDevices = [:]
    def readingClosure = { response -> response.data.each { device ->
    		particleDevices.put(device.id, device.name)  
        }
	}
    httpGet("${state.particleAPIUri}/devices?access_token=${state.particleToken}", readingClosure)
 	return particleDevices
}

def populateParticleToken() {
	if (!state.particleToken) {
    	httpPost(uri: "https://particle:particle@api.particle.io/oauth/token",
            	 body: [grant_type: "password",
                        username: settings.particleUsername,
                        password: settings.particlePassword,
                        expires_in: 2592000]
                ) { response -> state.particleToken = response.data.access_token }
        log.debug("New Particle.io auth token obtained for $particleUsername")
    }
}

def populateSmartThingsAccessToken() {
	if (!state.accessToken) {
    	createAccessToken()
        log.debug("Created access token")
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"    
	state.appURL = "https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/${state.particleEventName}?access_token=${state.accessToken}"
    checkWebhook()
	initialize()
}

def updated() {
    unsubscribe()
    checkWebhook()
    initialize()
}

def uninstalled() {
    log.debug "Uninstalling Photon RF Gateway"
    deleteWebhook()
    deleteAccessToken()
    deleteChildDevices()
    log.debug "Photon RF Gateway Uninstalled"
}

def initialize() {
	for (int i = 1; i <= state.maxNumDevices; i++) {
    	def name = "device${i}"
        def label = settings."device${i}Name"
        def type = settings."device${i}Type"
        def code = settings."device${i}Code"

        if (label != null && type != null && code != null) {
            def deviceType
        	switch(type) {
                case "Motion Sensor":
                    deviceType = "433 Motion"
                    break
            }
            
            if (!deviceType) {
            	log.debug "No device type ${type}"
            	continue
            }
            
            def d = getChildDevice(name)
            if (d && !d.displayName.equals(label)) {
            	log.debug "Device name changed from ${d} to ${label}"
            	deleteChildDevice(name)
                d = null
            }
            if (!d) {
            	log.debug "Creating device ${label}"
            	d = addChildDevice("gpete", deviceType, name, null, [label: label, name: name])
                d.inactivate()
                //d.take()
            }
        }
        else if (getChildDevice(name)) {
        	log.debug "Removing cleared device ${name}"
        	deleteChildDevice(name)
        }
    }
    //def d = addChildDevice("gpete", "433 Motion", "testMotion", null, [label:"motionLabel", name:"motionName"])
    //def devices = getChildDevices()
    devices.each {log.debug it.deviceNetworkId}
}

def parseIncomingData() {
	def data = new groovy.json.JsonSlurper().parseText(params.data)
    log.debug "Data packet: " + data
    for (int i = 1; i <= state.maxNumDevices; i++) {
    	def deviceCode = settings."device${i}Code"
    	if(data?.data.equalsIgnoreCase(deviceCode)) {
        	log.debug "Matched ${deviceCode} against ${data.data}"
        	def d = getChildDevice("device${i}")
            d?.activate()
            scheduleEndMotion(i)
        }
    }
}

def scheduleEndMotion(i) {
	if (!state.lastActiveTimes) {
    	state.lastActiveTimes = []
    }
    
    def needsToBeScheduled = true
    def timeoutMS = 4 * settings.inactiveCheckTime * 1000
    for (time in state.lastActiveTimes) {
    	if (time && time > now() - timeoutMS) {
        	needsToBeScheduled = false
            break
        }
    }
    
    state.lastActiveTimes[i] = now()
    
    if (needsToBeScheduled) {
    	runIn(settings.inactiveCheckTime, handleInactive)
    }
}

def handleInactive() {
    log.debug "Running handleEndMotion"
    def reschedule = false
    for (int i = 1; i <= state.lastActiveTimes?.size(); i++) {
        def lastActiveTime = state.lastActiveTimes[i]
        if (lastActiveTime && now() > lastActiveTime + (settings.inactiveCheckTime * 1000)) {
            state.lastActiveTimes[i] = null
            def d = getChildDevice("device${i}")
            d?.inactivate()
        }
        else if (lastActiveTime && !reschedule) {
            reschedule = true
        }
    }

    if (reschedule) {
        runIn(settings.inactiveCheckTime, handleInactive)
    }
}

void createWebhook() {
    log.debug("Creating a Particle webhook")

    httpPost(uri: "${state.particleAPIUri}/webhooks",
             body: [access_token: state.particleToken,
                    event: state.particleEventName,
                    url: state.appURL,
                    requestType: "POST",
                    mydevices: true]
            ) {response -> log.debug (response.data)}
}

void checkWebhook() {
	log.debug "Checking webhook..."
	if(!state.particleToken) {
    	log.warn "Trying to check webhook without a token"
        return
    }
    def foundHook = false
    httpGet(uri:"${state.particleAPIUri}/webhooks?access_token=${state.particleToken}") { response -> response.data.each
        { hook ->
            if (hook.event == state.particleEventName) {
                foundHook = true
                log.debug "Found existing webhook id: ${hook.id}"
            }
        }
    }
    if (!foundHook) {
        createWebhook()
    }
}

void deleteWebhook() {
    try {
        httpGet(uri:"${state.particleAPIUri}/webhooks?access_token=${state.particleToken}") { response -> response.data.each
            { hook ->
                if (hook.event == state.particleEventName) {
                    httpDelete(uri:"${state.particleAPIUri}/webhooks/${hook.id}?access_token=${state.particleToken}")
                    log.debug "Deleted the existing webhook with the id: ${hook.id} and the event name: ${state.particleEventName}"
                }
            }
        }
    }
    catch (all) {
        log.debug "Couldn't delete webhook: " + all
    }
}

void deleteAccessToken() {
    try {
        def authEncoded = "${settings.particleUsername}:${settings.particlePassword}".bytes.encodeBase64()
        def params = [
            uri: "${state.particleAPIUri}/access_tokens/${state.particleToken}",
            headers: [
                'Authorization': "Basic ${authEncoded}"
            ]
        ]

        httpDelete(params)
        log.debug "Deleted the existing Particle Access Token"
    }
    catch (all) {
        log.debug "Couldn't delete Particle Token: " + all
    }
}

def deleteChildDevices() {
	def delete = getChildDevices()
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}

def sendRFCommand(command) {
    httpPost(uri: "${state.particleAPIUri}/devices/${settings.particleDevice}/${state.particleSendCommandFunction}",
             body: [access_token: state.particleToken,
                    args: command]
            ) { response -> log.debug "sendRFCommand response: " + response.data }
}