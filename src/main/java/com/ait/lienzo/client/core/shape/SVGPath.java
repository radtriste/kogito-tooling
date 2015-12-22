/*
   Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.shape;

import java.util.Arrays;
import java.util.List;

import com.ait.lienzo.client.core.Attribute;
import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.Path2D;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.PathPartEntryJSO;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.shared.core.types.ShapeType;
import com.ait.tooling.nativetools.client.collection.NFastDoubleArrayJSO;
import com.google.gwt.json.client.JSONObject;

public class SVGPath extends Shape<SVGPath>
{
    private static final String[] COMMANDS = { "m", "M", "l", "L", "v", "V", "h", "H", "z", "Z", "c", "C", "q", "Q", "t", "T", "s", "S", "a", "A" };

    private String                m_path;

    private final PathPartList    m_list   = new PathPartList();

    public SVGPath(final String path)
    {
        super(ShapeType.SVG_PATH);

        setPath(path);
    }

    protected SVGPath(final JSONObject node, final ValidationContext ctx) throws ValidationException
    {
        super(ShapeType.SVG_PATH, node, ctx);

        setPath(getPath());
    }

    @Override
    public BoundingBox getBoundingBox()
    {
        return m_list.getBoundingBox();
    }

    @Override
    protected void drawWithoutTransforms(final Context2D context, double alpha, BoundingBox bounds)
    {
        if (m_list.size() < 1)
        {
            return;
        }
        final Attributes attr = getAttributes();

        if ((context.isSelection()) && (false == attr.isListening()))
        {
            return;
        }
        alpha = alpha * attr.getAlpha();

        if (alpha <= 0)
        {
            return;
        }
        final Path2D path = m_list.getPath2D();

        if (null != path)
        {
            if (path.isClosed())
            {
                fill(context, attr, alpha, path);
            }
            stroke(context, attr, alpha, path);
        }
        else
        {
            if (context.path(m_list))
            {
                fill(context, attr, alpha);
            }
            stroke(context, attr, alpha);
        }
    }

    @Override
    protected boolean prepare(final Context2D context, Attributes attr, double alpha)
    {
        if (m_list.size() < 1)
        {
            return false;
        }
        return true;
    }

    private final void parse(final String path)
    {
        parse(m_list, path);
    }

    public static final void parse(final PathPartList partlist, String path)
    {
        partlist.clear();

        path = path.replaceAll("\\s+", " ").trim();

        if (Path2D.isSupported())
        {
            partlist.setPath2D(new Path2D(path));
        }
        for (int n = 0; n < COMMANDS.length; n++)
        {
            path = path.replaceAll(COMMANDS[n] + " ", COMMANDS[n]);
        }
        path = path.replaceAll(" ", ",");

        for (int n = 0; n < COMMANDS.length; n++)
        {
            path = path.replaceAll(COMMANDS[n], "#" + COMMANDS[n]);
        }
        final String[] list = path.split("#");

        double cpx = 0;

        double cpy = 0;

        for (int n = 1, l = list.length; n < l; n++)
        {
            String str = list[n];

            char chr = str.charAt(0);

            str = str.substring(1).replaceAll(",-", "-").replaceAll("-", ",-").replaceAll("e,-", "e-");

            final String[] pts = str.split(",");

            int beg = 0;

            if ((pts.length > 0) && (pts[0].isEmpty()))
            {
                beg = 1;
            }
            final NFastDoubleArrayJSO source = NFastDoubleArrayJSO.make();

            for (int i = beg, z = pts.length; i < z; i++)
            {
                source.push(Double.valueOf(pts[i]).doubleValue());
            }
            PathPartEntryJSO prev;

            double ctx, cty;

            while (source.size() > 0)
            {
                int cmd = PathPartEntryJSO.UNDEFINED_PATH_PART;

                final NFastDoubleArrayJSO points = NFastDoubleArrayJSO.make();

                switch (chr)
                {
                    case 'l':
                        cpx += source.shift();

                        cpy += source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.LINETO_ABSOLUTE;
                        break;
                    case 'L':
                        cpx = source.shift();

                        cpy = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.LINETO_ABSOLUTE;
                        break;
                    case 'm':
                        final double dx = source.shift();

                        final double dy = source.shift();

                        cpx += dx;

                        cpy += dy;

                        final int size = partlist.size();

                        if (size > 2 && partlist.get(size - 1).getCommand() == PathPartEntryJSO.CLOSE_PATH_PART)
                        {
                            for (int idx = size - 2; idx >= 0; idx--)
                            {
                                prev = partlist.get(idx);

                                if (prev.getCommand() == PathPartEntryJSO.MOVETO_ABSOLUTE)
                                {
                                    cpx = prev.getPoints().get(0) + dx;

                                    cpy = prev.getPoints().get(1) + dy;

                                    break;
                                }
                            }
                        }
                        points.push(cpx);

                        points.push(cpy);

                        chr = 'l';

                        cmd = PathPartEntryJSO.MOVETO_ABSOLUTE;
                        break;
                    case 'M':
                        cpx = source.shift();

                        cpy = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        chr = 'L';

                        cmd = PathPartEntryJSO.MOVETO_ABSOLUTE;
                        break;
                    case 'h':
                        cpx += source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.LINETO_ABSOLUTE;
                        break;
                    case 'H':
                        cpx = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.LINETO_ABSOLUTE;
                        break;
                    case 'v':
                        cpy += source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.LINETO_ABSOLUTE;
                        break;
                    case 'V':
                        cpy = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.LINETO_ABSOLUTE;
                        break;
                    case 'C':
                        points.push(source.shift());

                        points.push(source.shift());

                        points.push(source.shift());

                        points.push(source.shift());

                        cpx = source.shift();

                        cpy = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.BEZIER_CURVETO_ABSOLUTE;
                        break;
                    case 'c':
                        points.push(cpx + source.shift());

                        points.push(cpy + source.shift());

                        points.push(cpx + source.shift());

                        points.push(cpy + source.shift());

                        cpx += source.shift();

                        cpy += source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.BEZIER_CURVETO_ABSOLUTE;
                        break;
                    case 'S':
                        ctx = cpx;

                        cty = cpy;

                        prev = partlist.get(partlist.size() - 1);

                        if (prev.getCommand() == PathPartEntryJSO.BEZIER_CURVETO_ABSOLUTE)
                        {
                            ctx = cpx + (cpx - prev.getPoints().get(2));

                            cty = cpy + (cpy - prev.getPoints().get(3));
                        }
                        points.push(ctx);

                        points.push(cty);

                        points.push(source.shift());

                        points.push(source.shift());

                        cpx = source.shift();

                        cpy = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.BEZIER_CURVETO_ABSOLUTE;
                        break;
                    case 's':
                        ctx = cpx;

                        cty = cpy;

                        prev = partlist.get(partlist.size() - 1);

                        if (prev.getCommand() == PathPartEntryJSO.BEZIER_CURVETO_ABSOLUTE)
                        {
                            ctx = cpx + (cpx - prev.getPoints().get(2));

                            cty = cpy + (cpy - prev.getPoints().get(3));
                        }
                        points.push(ctx);

                        points.push(cty);

                        points.push(cpx + source.shift());

                        points.push(cpy + source.shift());

                        cpx += source.shift();

                        cpy += source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.BEZIER_CURVETO_ABSOLUTE;
                        break;
                    case 'Q':
                        points.push(source.shift());

                        points.push(source.shift());

                        cpx = source.shift();

                        cpy = source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.QUADRATIC_CURVETO_ABSOLUTE;
                        break;
                    case 'q':
                        points.push(cpx + source.shift());

                        points.push(cpy + source.shift());

                        cpx += source.shift();

                        cpy += source.shift();

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.QUADRATIC_CURVETO_ABSOLUTE;
                        break;
                    case 'T':
                        ctx = cpx;

                        cty = cpy;

                        prev = partlist.get(partlist.size() - 1);

                        if (prev.getCommand() == PathPartEntryJSO.QUADRATIC_CURVETO_ABSOLUTE)
                        {
                            ctx = cpx + (cpx - prev.getPoints().get(0));

                            cty = cpy + (cpy - prev.getPoints().get(1));
                        }
                        cpx = source.shift();

                        cpy = source.shift();

                        points.push(ctx);

                        points.push(cty);

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.QUADRATIC_CURVETO_ABSOLUTE;
                        break;
                    case 't':
                        ctx = cpx;

                        cty = cpy;

                        prev = partlist.get(partlist.size() - 1);

                        if (prev.getCommand() == PathPartEntryJSO.QUADRATIC_CURVETO_ABSOLUTE)
                        {
                            ctx = cpx + (cpx - prev.getPoints().get(0));

                            cty = cpy + (cpy - prev.getPoints().get(1));
                        }
                        cpx += source.shift();

                        cpy += source.shift();

                        points.push(ctx);

                        points.push(cty);

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.QUADRATIC_CURVETO_ABSOLUTE;
                        break;
                    case 'A':
                    {
                        final double rx = source.shift();

                        final double ry = source.shift();

                        final double ps = source.shift();

                        final double fa = source.shift();

                        final double fs = source.shift();

                        final double x1 = cpx;

                        final double y1 = cpy;

                        cpx = source.shift();

                        cpy = source.shift();

                        PathPartList.convertEndpointToCenterParameterization(points, x1, y1, cpx, cpy, fa, fs, rx, ry, ps);

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.ARCTO_ABSOLUTE;
                        break;
                    }
                    case 'a':
                    {
                        final double rx = source.shift();

                        final double ry = source.shift();

                        final double ps = source.shift();

                        final double fa = source.shift();

                        final double fs = source.shift();

                        final double x1 = cpx;

                        final double y1 = cpy;

                        cpx += source.shift();

                        cpy += source.shift();

                        PathPartList.convertEndpointToCenterParameterization(points, x1, y1, cpx, cpy, fa, fs, rx, ry, ps);

                        points.push(cpx);

                        points.push(cpy);

                        cmd = PathPartEntryJSO.ARCTO_ABSOLUTE;
                        break;
                    }
                }
                if (cmd != PathPartEntryJSO.UNDEFINED_PATH_PART)
                {
                    partlist.push(PathPartEntryJSO.make(cmd, points));
                }
            }
            if ((chr == 'z') || (chr == 'Z'))
            {
                partlist.Z();
            }
        }
    }

    public String getPath()
    {
        return getAttributes().getPath();
    }

    public SVGPath setPath(final String path)
    {
        getAttributes().setPath(path);

        if (false == path.equals(m_path))
        {
            parse(m_path = path);
        }
        return this;
    }

    @Override
    public List<Attribute> getBoundingBoxAttributes()
    {
        return Arrays.asList(Attribute.PATH);
    }

    public static class SVGPathFactory extends ShapeFactory<SVGPath>
    {
        public SVGPathFactory()
        {
            super(ShapeType.SVG_PATH);

            addAttribute(Attribute.PATH, true);
        }

        @Override
        public SVGPath create(final JSONObject node, final ValidationContext ctx) throws ValidationException
        {
            return new SVGPath(node, ctx);
        }
    }
}
