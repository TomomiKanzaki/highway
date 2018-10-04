package co.to_kanzaki.highwayfee.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementArray
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Result", strict = false)
class GetRouteHighWay{
    @field:Element(name = "Status", required = false)var Status: String = ""
    @field:Element(name = "From", required = false)var From: String = ""
    @field:Element(name = "To", required = false)var To: String = ""
    @field:Element(name = "CarType", required = false)var CarType: String = ""
//    @field:ElementList(entry = "FromICs", required = false, inline = true)var FromICs: List<FromICs>? = null
//    @field:ElementList(entry = "ToICs", required = false, inline = true)var ToICs: List<ToICs>? = null
//    @field:ElementList(entry = "StateICs", required = false, inline = true)var StateICs: List<StateICs>? = null
    @field:Element(name = "SortBy", required = false)var SortBy: String = ""
    @field:ElementList(entry = "Routes", required = false)var Routes: List<Route>? = null
}

class FromICs{
    @field:Element(name = "No", required = false)val No: Int = -1
    @field:Element(name = "IC", required = false)val IC: String = ""
}

class ToICs{
    @field:Element(name = "No", required = false)val No: Int = -1
    @field:Element(name = "IC", required = false)val IC: String = ""
}

class StateICs{
    @field:Element(name = "No", required = false)val No: Int = -1
    @field:Element(name = "IC", required = false)val IC: String = ""
}

//@Root(strict = false)
//class Routes(){
//    @field:ElementList(required = false, inline = true)var route: List<Route>? = null
//}

@Root(strict = false)
class Route{
    @field:Element(name = "RouteNo", required = false)var RouteNo: Int = -1
    @field:Element(name = "Summary", required = false)var Summary: Summary? = null
    @field:Element(name = "Details", required = false)var Details: Details? = null
}

@Root(strict = false)
class Summary{
    @field:Element(name = "TotalToll", required = false)var TotalToll: Int = -1
    @field:Element(name = "TotalTime", required = false)var TotalTime: Int = -1
    @field:Element(name = "TotalLength", required = false)var TotalLength: Double = 0.0
}

@Root(strict = false)
class Details{
    @field:Element(name = "No", required = false)var No: Int = -1
    @field:ElementList(entry = "Section", required = false, inline = true)var Section: List<Section>? = null
}

@Root(strict = false)
class Section{
    @field:Element(name = "Order", required = false)var Order: Int = -1
    @field:Element(name = "From", required = false)var From: String = ""
    @field:Element(name = "To", required = false)var To: String = ""
    @field:Element(name = "Length", required = false)var Length: Double = 0.0
    @field:ElementList(entry = "SubSections", required = false)var SubSections: List<SubSection>? = null
    @field:ElementList(entry = "Tolls", required = false, inline = true)var Tolls: List<Tolls>? = null
    @field:Element(name = "Time", required = false)var Time: Int = -1
}

@Root(strict = false)
class SubSection{
    @field:Element(name = "From", required = false)var From: String = ""
    @field:Element(name = "To", required = false)var To: String = ""
    @field:Element(name = "Road", required = false)var Road: String = ""
    @field:Element(name = "Length", required = false)var Length: Double = 0.0
    @field:Element(name = "Time", required = false)var Time: Double = 0.0
}

@Root(strict = false)
class Tolls{
    @field:Element(name = "No", required = false)var No: Int = -1
    @field:ElementList(entry = "Toll", required = false, inline = true)var Toll: List<String?>? = null
}