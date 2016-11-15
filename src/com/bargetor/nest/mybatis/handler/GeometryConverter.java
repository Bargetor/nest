package com.bargetor.nest.mybatis.handler;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import com.vividsolutions.jts.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by bargetor on 2016/11/14.
 */
public class GeometryConverter {
    /**
     * Little endian or Big endian
     */
    private static int byteOrder = ByteOrderValues.LITTLE_ENDIAN;
    /**
     * Precision model
     */
    private static PrecisionModel precisionModel = new PrecisionModel();
    /**
     * Coordinate sequence factory
     */
    private static CoordinateSequenceFactory coordinateSequenceFactory = CoordinateArraySequenceFactory.instance();
    /**
     * Output dimension
     */
    private static int outputDimension = 2;
    /**
     * Convert byte array containing SRID + WKB Geometry into Geometry object
     */
    public static Geometry from(Object databaseObject) {
        final byte[] bytes = (byte[]) databaseObject;
        if (bytes == null) return null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            // Read SRID
            byte[] sridBytes = new byte[4];
            inputStream.read(sridBytes);
            int srid = ByteOrderValues.getInt(sridBytes, byteOrder);

            // Prepare Geometry factory
            GeometryFactory geometryFactory = new GeometryFactory(precisionModel, srid, coordinateSequenceFactory);

            // Read Geometry
            WKBReader wkbReader = new WKBReader(geometryFactory);
            Geometry geometry = wkbReader.read(new InputStreamInStream(inputStream));
            return geometry;
        } catch(IOException | ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Convert Geometry object into byte array containing SRID + WKB Geometry
     */
    public static Object to(Geometry userObject) {
        if (userObject == null) return null;
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Write SRID
            byte[] sridBytes = new byte[4];
            ByteOrderValues.putInt(userObject.getSRID(), sridBytes, byteOrder);
            outputStream.write(sridBytes);
            // Write Geometry
            WKBWriter wkbWriter = new WKBWriter(outputDimension, byteOrder);
            wkbWriter.write(userObject, new OutputStreamOutStream(outputStream));
            return outputStream.toByteArray();
        } catch(IOException ioe) {
            throw new IllegalArgumentException(ioe);
        }
    }
}
