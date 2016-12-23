/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iot.dataServices;



import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1
 */
@Path("/service")
public class IOTDataService {

    @GET
    @Path("/get/dimension")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDimension() {

        // TODO: Implementation for HTTP GET request
        double xM = Connection.getMax("X");
        double xm=Connection.getMin("X");
        double yM = Connection.getMax("Y");
        double ym=Connection.getMin("Y");
        JSONObject jobj= new JSONObject();
        try{

            jobj.put("maxX",xM);
            jobj.put("maxY",yM);
            jobj.put("minX",xm);
            jobj.put("minY",ym);
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(jobj.toString()).build();

        return r;
    }



    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(@QueryParam("start") double st, @QueryParam("end") double ed) {

        // TODO: Implementation for HTTP GET request
        double start = st;
        double end = ed;
        DataHandler dh = new DataHandler();
        JSONObject response = dh.loadData(start, end);
        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();

        return r;


    }

    @GET
    @Path("/time/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInitStartTime() {
        DataHandler dh = new DataHandler();
        String stt = dh.getInitStartTime();
        // TODO: Implementation for HTTP GET request
        JSONObject response = new JSONObject();
        try{
            response.put("stt", stt);
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();


    }



    @POST
    @Path("/file/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormParam("file") File file){
        File output = new File("output.txt");
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(file).getChannel();
            outputChannel = new FileOutputStream(output).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputChannel.close();
            outputChannel.close();
            File file2 = new File("typicalScene.txt");
            if(file2.delete()){
                System.out.println(file2.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }

        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
            System.out.println("File Not Found");
        }
        catch (IOException io){
            io.printStackTrace();
            System.out.println(io);
        }
        typicalScene ts = new typicalScene();
        ts.divideData();


        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").build();
        return r;
    }
    @GET
    @Path("/time/end")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEndTime() {
        JSONObject response = new JSONObject();
        DataHandler dh = new DataHandler();


        try{
            System.out.println("try");
            response.put("stt", dh.getEndTime());
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();
    }



    @GET
    @Path("/data/chunk")
    public Response getDataChunk(@QueryParam("start") double st, @QueryParam("end") double ed) {
        double start = st;
        double end = ed;
        DataHandler dh = new DataHandler();
        JSONObject response = dh.loadDataChunk(start, end);
        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();

        return r;
    }
    @GET
    @Path("/data/density")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataDensity(@QueryParam("start") double st, @QueryParam("end") double ed) {

        // TODO: Implementation for HTTP GET request
        double start = st;
        double end = ed;
        DataHandler dh = new DataHandler();
        JSONObject response = dh.getDataDensity(start, end);
        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();

        return r;


    }
    @GET
    @Path("/data/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataCount(@QueryParam("start") double st, @QueryParam("gap") double gap,@QueryParam("sensor") int sensor ) {

        // TODO: Implementation for HTTP GET request
        double start = st;


        DataHandler dh = new DataHandler();
        JSONObject response=dh.getFrequency(start,gap,sensor);
        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();

        return r;


    }
    @GET
    @Path("/typical/scene")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScene(@QueryParam("day") String day, @QueryParam("hour") String hour) {

        typicalScene ts = new typicalScene();
        JSONObject response =ts.getTypicalScene(day,hour);

        Response r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(response.toString()).build();
        return r;


    }







    // save uploaded file to new location

}
