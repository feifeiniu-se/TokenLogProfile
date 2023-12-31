<?xml version="1.0"?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, loggenerate
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="../../common/css/forrest.css.xslt"/>
<!-- xsl:output is not used by Forrest but makes it possible to debug the 
       stylesheet in standalone editors -->
  <xsl:output method = "text"  omit-xml-declaration="yes"  />
<!-- ==================== main block colors ============================ -->
  <xsl:template match="color[@name='header']">
#top          { background-color: <xsl:value-of select="@value"/>;}  
</xsl:template>
  <xsl:template match="color[@name='tab-selected']"> 
#top .header .current { background-color: <xsl:value-of select="@value"/>;} 
#top .header .current a:link {  color: <xsl:value-of select="@link"/>;  }
#top .header .current a:visited { color: <xsl:value-of select="@vlink"/>; }
#top .header .current a:hover { color: <xsl:value-of select="@hlink"/>; }
</xsl:template>
  <xsl:template match="color[@name='tab-unselected']"> 
#tabs li      { background-color: <xsl:value-of select="@value"/> ;} 
#tabs li a:link {  color: <xsl:value-of select="@link"/>;  }
#tabs li a:visited { color: <xsl:value-of select="@vlink"/>; }
#tabs li a:hover { color: <xsl:value-of select="@hlink"/>; }
</xsl:template>
  <xsl:template match="color[@name='subtab-selected']">
#level2tabs a.selected      { background-color: <xsl:value-of select="@value"/> ;} 
#level2tabs a:link {  color: <xsl:value-of select="@link"/>;  }
#level2tabs a:visited { color: <xsl:value-of select="@vlink"/>; }
#level2tabs a:hover { color: <xsl:value-of select="@hlink"/>; }
</xsl:template>
  <xsl:template match="color[@name='subtab-unselected']">
#level2tabs { background-color: <xsl:value-of select="@value"/>;}
#level2tabs a.unselected:link {  color: <xsl:value-of select="@link"/>;  }
#level2tabs a.unselected:visited { color: <xsl:value-of select="@vlink"/>; }
#level2tabs a.unselected:hover { color: <xsl:value-of select="@hlink"/>; }
</xsl:template>
  <xsl:template match="color[@name='heading']">
.heading { background-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='subheading']">
.boxed { background-color: <xsl:value-of select="@value"/>;} 
.underlined_5 	{border-bottom: solid 5px <xsl:value-of select="@value"/>;}
.underlined_10 	{border-bottom: solid 10px <xsl:value-of select="@value"/>;}
table caption { 
background-color: <xsl:value-of select="@value"/>; 
color: <xsl:value-of select="@font"/>;
}
</xsl:template>
  <xsl:template match="color[@name='feedback']">    
#feedback {
color: <xsl:value-of select="@font"/>;
background: <xsl:value-of select="@value"/>;
text-align: <xsl:value-of select="@align"/>;
}
#feedback #feedbackto {
color: <xsl:value-of select="@font"/>;
}   
</xsl:template>
  <xsl:template match="color[@name='breadtrail']">
#main .breadtrail {
background: <xsl:value-of select="@value"/>; 
color: <xsl:value-of select="@font"/>;
}
#main .breadtrail a:link {  color: <xsl:value-of select="@link"/>;  }
#main .breadtrail a:visited { color: <xsl:value-of select="@vlink"/>; }
#main .breadtrail a:hover { color: <xsl:value-of select="@hlink"/>; }
#top .breadtrail {
background: <xsl:value-of select="@value"/>; 
color: <xsl:value-of select="@font"/>;
}
#top .breadtrail a:link {  color: <xsl:value-of select="@link"/>;  }
#top .breadtrail a:visited { color: <xsl:value-of select="@vlink"/>; }
#top .breadtrail a:hover { color: <xsl:value-of select="@hlink"/>; }    
</xsl:template>
<!--Fix for other (old) profiles-->
  <xsl:template match="color[@name='navstrip']">
#publishedStrip { 
color: <xsl:value-of select="@font"/>;
background: <xsl:value-of select="@value"/>; 
}
</xsl:template>
<!--has to go after the nav-strip (no 'navstrip')-->
  <xsl:template match="color[@name='published']">
#publishedStrip { 
color: <xsl:value-of select="@font"/>;
background: <xsl:value-of select="@value"/>; 
}
</xsl:template>
  <xsl:template match="color[@name='toolbox']">
#menu .menupagetitle  { background-color: <xsl:value-of select="@value"/>;
  color: <xsl:value-of select="@font"/>;}
</xsl:template>
  <xsl:template match="color[@name='border']">
#menu           { border-color: <xsl:value-of select="@value"/>;}
#menu .menupagetitle  { border-color: <xsl:value-of select="@value"/>;}
#menu .menupageitemgroup  { border-color: <xsl:value-of select="@value"/>;}
</xsl:template>
  <xsl:template match="color[@name='menu']">
#menu      { background-color: <xsl:value-of select="@value"/>;} 
#menu  {  color: <xsl:value-of select="@font"/>;} 
#menu a:link {  color: <xsl:value-of select="@link"/>;} 
#menu a:visited {  color: <xsl:value-of select="@vlink"/>;} 
#menu a:hover {  
background-color: <xsl:value-of select="@value"/>;
color: <xsl:value-of select="@hlink"/>;} 
</xsl:template>
  <xsl:template match="color[@name='dialog']"> 
#menu .menupageitemgroup     { 
background-color: <xsl:value-of select="@value"/>;
}
#menu .menupageitem {
color: <xsl:value-of select="@font"/>;
} 
#menu .menupageitem a:link {  color: <xsl:value-of select="@link"/>;} 
#menu .menupageitem a:visited {  color: <xsl:value-of select="@vlink"/>;} 
#menu .menupageitem a:hover {  
background-color: <xsl:value-of select="@value"/>;
color: <xsl:value-of select="@hlink"/>;
}
</xsl:template>
  <xsl:template match="color[@name='menuheading']">
#menu h1 {
color: <xsl:value-of select="@font"/>;
background-color: <xsl:value-of select="@value"/>;
}   
</xsl:template>
  <xsl:template match="color[@name='searchbox']"> 
#top .searchbox { 
background-color: <xsl:value-of select="@value"/> ;
color: <xsl:value-of select="@font"/>; 
} 
</xsl:template>
  <xsl:template match="color[@name='body']">
body{ 
background-color: <xsl:value-of select="@value"/>;
color: <xsl:value-of select="@font"/>;
} 
a:link { color:<xsl:value-of select="@link"/>} 
a:visited { color:<xsl:value-of select="@vlink"/>} 
a:hover { color:<xsl:value-of select="@hlink"/>} 

</xsl:template>
  <xsl:template match="color[@name='footer']"> 
#footer       { background-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
<!-- ==================== other colors ============================ -->
  <xsl:template match="color[@name='highlight']"> 
.highlight        { background-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='fixme']"> 
.fixme        { border-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='note']"> 
.note         { border-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='warning']"> 
.warning         { border-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='code']"> 
.code         { border-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='table']"> 
.ForrestTable      { background-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
  <xsl:template match="color[@name='table-cell']"> 
.ForrestTable td   { background-color: <xsl:value-of select="@value"/>;} 
</xsl:template>
</xsl:stylesheet>
