/* 
    JSPWiki - a JSP-based WikiWiki clone.

    Copyright (C) 2002 Janne Jalkanen (Janne.Jalkanen@iki.fi)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2.1 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.ecyrd.jspwiki.plugin;

import java.util.*;
import com.ecyrd.jspwiki.*;
import com.ecyrd.jspwiki.attachment.AttachmentManager;
import com.ecyrd.jspwiki.attachment.Attachment;
import com.ecyrd.jspwiki.providers.ProviderException;

/**
 *  Provides an image plugin for better control than is possible with
 *  a simple image inclusion.
 *
 *  @author Janne Jalkanen
 *  @since 2.1.4.
 */
public class Image
    implements WikiPlugin
{
    public static final String PARAM_SRC      = "src";
    public static final String PARAM_ALIGN    = "align";
    public static final String PARAM_HEIGHT   = "height";
    public static final String PARAM_WIDTH    = "width";
    public static final String PARAM_ALT      = "alt";
    public static final String PARAM_CAPTION  = "caption";

    public String execute( WikiContext context, Map params )
        throws PluginException
    {
        WikiEngine engine = context.getEngine();
        String src     = (String) params.get( PARAM_SRC );
        String align   = (String) params.get( PARAM_ALIGN );
        String ht      = (String) params.get( PARAM_HEIGHT );
        String wt      = (String) params.get( PARAM_WIDTH );
        String alt     = (String) params.get( PARAM_ALT );
        String caption = (String) params.get( PARAM_CAPTION );

        if( src == null )
        {
            throw new PluginException("Parameter 'src' is required for Image plugin");
        }

        try
        {
            AttachmentManager mgr = engine.getAttachmentManager();
            Attachment        att = mgr.getAttachmentInfo( context, src );

            if( att != null )
            {
                src = engine.getBaseURL()+"attach?page="+att.getName();
            }
        }
        catch( ProviderException e )
        {
            throw new PluginException( "Attachment info failed: "+e.getMessage() );
        }

        StringBuffer result = new StringBuffer();

        result.append( "<table border=0 class=\"imageplugin\"" );
        if( align != null ) result.append(" align=\""+align+"\"");
        result.append( ">\n" );

        if( caption != null ) 
        {
            result.append("<caption align=bottom>"+TextUtil.replaceEntities(caption)+"</caption>\n");
        }


        result.append( "<tr><td>" );
        result.append( "<img src=\""+src+"\"" );
       
        if( ht != null )    result.append(" height=\""+ht+"\"");
        if( wt != null )    result.append(" width=\""+wt+"\"");
        if( alt != null )   result.append(" alt=\""+alt+"\"");

        result.append("/></td></tr>\n");

        result.append("</table>\n");

        return result.toString();
    }
}
