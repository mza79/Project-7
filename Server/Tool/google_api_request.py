import httplib
import json
import logging

__author__ = 'Nazzareno'

from setting import static_variable

class Google_api_request:

    @staticmethod
    def request_place(latitude,longitude):

        #url = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?location='
        url = '/maps/api/place/nearbysearch/json?location='
        url_place = url+latitude+","+longitude
        url_radius = url_place+"&radius=500"
        url_type = url_radius+"&types=subway_station|train_station"
        my_key = "key=" + static_variable.google_api_key

        conn = httplib.HTTPSConnection("maps.googleapis.com")
        conn.request("GET", url_type+"&"+my_key)
        r1 = conn.getresponse()

        response = json.loads(r1.read())

        if static_variable.DEBUG:
            logging.debug("Status request google place --> "+response["status"])
        if response["status"] == "OK":
            if static_variable.DEBUG:
                logging.debug("Station near the place")
            return 1
        else:
            return 0