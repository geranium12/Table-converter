<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <style>
                    h1 {
                    font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
                    color: #039;
                    }
                    table {
                    font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
                    border: 1px solid #69c;
                    border-collapse: collapse;
                    }
                    th {
                    font-weight: normal;
                    color: #039;
                    padding: 10px;
                    }
                    td {
                    color: #669;
                    border-top: 1px dashed #fff;
                    padding: 10px;
                    background:#ccddff;
                    }
                    tr:hover td {
                    background: #99bcff;
                    }
                </style>
            </head>
            <body>
                <h1 align="center">House adverts</h1>
                <table border="3" align="center">
                    <tfoot>
                        <tr>
                            <td colspan="6">
                                <strong>Sum price</strong>
                            </td>
                            <td>
                                <xsl:value-of select="sum(root/house/price)"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6">
                                <strong>Average price</strong>
                            </td>
                            <td>
                                <xsl:value-of select="sum(root/house/price) div count(root/house/price)"/>
                            </td>
                        </tr>
                    </tfoot>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Floors</th>
                        <th>Rooms</th>
                        <th>Built year</th>
                        <th>Car places</th>
                        <th>Price</th>
                    </tr>
                    <xsl:for-each select="root/house">
                        <tr>
                            <td>
                                <xsl:value-of select="@id"/>
                            </td>
                            <td>
                                <xsl:value-of select="type"/>
                            </td>
                            <td>
                                <xsl:value-of select="floors"/>
                            </td>
                            <td>
                                <xsl:value-of select="rooms"/>
                            </td>
                            <td>
                                <xsl:value-of select="yearBuilt"/>
                            </td>
                            <td>
                                <xsl:value-of select="garageCars"/>
                            </td>
                            <td>
                                <xsl:value-of select="price"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>