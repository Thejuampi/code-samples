import jnettool.tools.elements.NetworkElement
import jnettool.tools.Routing
import jnettool.tools.RouteInspector

ne = jnnettool.tools.elements.NetworkElement( '171.0.2.45' )

try:
       routing_table = ne.getRoutingTable()
except jnettool.tools.elements.MissingVar:
       logging.exception('''No routing table found''')
       ne.cleanup('''rollback''')
else:
       num_routes = routing_table.getSize()  # determine table size
       for RToffset in range( num_routes ):
              route = routing_table.getRouteByIndex( RToffset )
              name = route.getName()                 # route name
              ipaddr = route.getIPAddr()             # ip address
              print("%15s -> %s" % (name,ipaddr))  # format nicely
finally:
       ne.cleanup('''cleanup''') # lockin changes
       ne.disconnect()

