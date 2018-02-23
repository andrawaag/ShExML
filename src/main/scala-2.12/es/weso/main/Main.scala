package es.weso.main

import es.weso.shexml.MappingLauncher

/**
  * Created by herminio on 22/12/17.
  */
object Main {

  def main(args: Array[String]): Unit = {
    val example =
      """
        |PREFIX : <http://ex.com/>
        |SOURCE performances_json <https://cdn.rawgit.com/herminiogg/ShExML/f1fa70f6/src/test/resources/events.json>
        |SOURCE events_xml <https://cdn.rawgit.com/herminiogg/ShExML/f1fa70f6/src/test/resources/events.xml>
        |QUERY performances <$.Performances[*].Perf_ID>
        |QUERY events </Events/Exhibition/@id>
        |QUERY venues_ids <$.Performances[*].Venue.Venue_ID>
        |QUERY venues_names </Events/Exhibition/Venue>
        |QUERY venues_names_json <$.Performances[*].Venue.Name>
        |QUERY lat_json <$.Performances[*].Location.lat>
        |QUERY lat_xml </Events/Exhibition/Location/lat>
        |QUERY long_json <$.Performances[*].Location.long>
        |QUERY long_xml </Events/Exhibition/Location/long>
        |EXPRESSION performances_union <$performances_json.performances UNION $events_xml.events>
        |EXPRESSION venues_union <$performances_json.venues_ids UNION $events_xml.venues_names JOIN $performances_json.venues_names_json>
        |EXPRESSION location_union <$performances_json.lat_json + "-" + $performances_json.long_json UNION
        |                             $events_xml.lat_xml + "-" + $events_xml.long_xml>
        |EXPRESSION lat_union <$performances_json.lat_json UNION $events_xml.lat_xml>
        |EXPRESSION long_union <$performances_json.long_json UNION $events_xml.long_xml>
        |
        |:Performance :[performances_union] {
        |  :venue :[venues_union] ;
        |  :location @:Location ;
        |}
        |
        |:Location :[location_union] {
        |  :lat [lat_union] ;
        |  :long [long_union] ;
        |}
      """.stripMargin
    val mappingLauncher = new MappingLauncher()
    val output = mappingLauncher.launchMapping(example, "TURTLE")
    println(output)
  }

}
