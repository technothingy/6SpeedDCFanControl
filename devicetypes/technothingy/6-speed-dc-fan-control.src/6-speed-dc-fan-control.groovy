/**
 *  6 Speed DC Fan Control
 *
 *  Copyright 2017 Sean Savage
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
 
import groovy.json.JsonBuilder

metadata {
	definition (name: "6 Speed DC Fan Control", namespace: "technothingy", author: "Sean Savage") {
		capability "Actuator"
//		capability "Switch"
		
		command "speed1"
		command "speed2"
		command "speed3"
		command "speed4"
		command "speed5"
		command "speed6"
		command "speed0"
		command "reverse"
		
		preferences {
			input("ip", "string", title:"WebService IP Address", description: "WebService IP Address", required: true, displayDuringSetup: true)
			input("port", "string", title:"WebService Port", description: "WebService Port", defaultValue: 5000 , required: true, displayDuringSetup: true)
		}
		
		tiles {
			/*standardTile("tileName", "device.switch", width: 6, height: 4) {
				state "off", label: "off", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
				state "on", label: "on", icon: "st.switches.switch.on", backgroundColor: "#00a0dc"
			}*/
			
			standardTile("speed1", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed1", label: "Speed 1", action: "speed1", icon: ""
			}
			
			standardTile("speed2", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed2", label: "Speed 2", action: "speed2", icon: ""
			}
			
			standardTile("speed3", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed3", label: "Speed 3", action: "speed3", icon: ""
			}
			
			standardTile("speed4", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed4", label: "Speed 4", action: "speed4", icon: ""
			}
			
			standardTile("speed5", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed5", label: "Speed 5", action: "speed5", icon: ""
			}
			
			standardTile("speed6", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed6", label: "Speed 6", action: "speed6", icon: ""
			}
			
			standardTile("speed0", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "speed0", label: "Off", action: "speed0", icon: ""
			}
			
			standardTile("reverse", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
				state "reverse", label: "reverse", action: "reverse", icon: ""
			}
		}
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}


def sendLevel(level) {

	def json = new JsonBuilder()
	json.call("speed":level)

	def headers = [:] 
	headers.put("HOST", "$ip:$port")
	headers.put("Content-Type", "application/json")

	log.debug "The Header is $headers"

	def method = "POST"

	try {
		def hubAction = new physicalgraph.device.HubAction(
			method: method,
			path: "/manage",
			body: json.content,
			headers: headers,
		)
	
		log.debug hubAction
		hubAction
	}
	catch (Exception e) {
		log.debug "Hit Exception $e on $hubAction"
	}
}

// gets the address of the Hub
private getCallBackAddress() {
    return device.hub.getDataValue("localIP") + ":" + device.hub.getDataValue("localSrvPortTCP")
}

// gets the address of the device
private getHostAddress() {
    def ip = getDataValue("ip")
    def port = getDataValue("port")

    if (!ip || !port) {
        def parts = device.deviceNetworkId.split(":")
        if (parts.length == 2) {
            ip = parts[0]
            port = parts[1]
        } else {
            log.warn "Can't figure out ip and port for device: ${device.id}"
        }
    }

    log.debug "Using IP: $ip and port: $port for device: ${device.id}"
    return convertHexToIP(ip) + ":" + convertHexToInt(port)
}

private Integer convertHexToInt(hex) {
    return Integer.parseInt(hex,16)
}

private String convertHexToIP(hex) {
    return [convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}

def speed1() {
	sendLevel(1)
}

def speed2() {
	sendLevel(2)
}

def speed3() {
	sendLevel(3)
}

def speed4() {
	sendLevel(4)
}

def speed5() {
	sendLevel(5)
}

def speed6() {
	sendLevel(6)
}

def speed0() {
	sendLevel(0)
}

def reverse() {
	sendLevel(-1)
}


