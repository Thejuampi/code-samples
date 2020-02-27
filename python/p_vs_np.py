import jnettool.tools.elements.NetworkElement
import jnettool.tools.Routing
import jnettool.tools.RouteInspector

ne = jnnettool.tools.elements.NetworkElement('171.0.2.45')
"""
in some way there is nothing wrong with this code
It works and it's readable.

On the other hand, the code on the bottom is profoundly better
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
