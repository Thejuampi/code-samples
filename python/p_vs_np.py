import jnettool.tools.elements.NetworkElement
import jnettool.tools.Routing
import jnettool.tools.RouteInspector

ne = jnnettool.tools.elements.NetworkElement('171.0.2.45')
"""
in some way there is nothing wrong with this code
It works and it's readable.

On the other hand, the code on the bottom is profoundly better

Code on the top would pass most people's coding standards, it would get checked in
and would live for a long time and other people would copy that style, and we would congratulate
we're being "compliant"

What is wrong is "ignoring the Gorilla"

Recommendations:
* avoid unnecessary packaging in favor of simple imports -> probably solved by using the * import (e.g. import java.lang.*)

* Custom Exceptions -> allows putting a name to the problem

* properties instead of getters -> this is not available in java, but we have @Getter and/or @Accessors(fluent=true)
 or simply define a routingTable() method
"""
try:
    routing_table = ne.getRoutingTable()
except jnettool.tools.elements.MissingVar:
    logging.exception('No routing table found')
    ne.cleanup('rollback')
else:
    num_routes = routing_table.getSize()
    for RToffset in range(num_routes):
        route = routing_table.getRouteByIndex(RToffset)
        name = route.getName()
        ipaddr = route.getIPAddr()
        print("%15s -> %s" % (name, ipaddr))
finally:
    ne.cleanup('commit')
    ne.disconnect()

#################################

from nettools import NetworkElement

with NetworkElement('171.0.2.45') as ne:
    for route in ne.routing_table:
        print("%15s -> %s" % (route.name, route.ipaddr))
