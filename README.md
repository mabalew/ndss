ndss
====

Network data storage servlet (NDSS) is a tool to store any information
you wish and to make it available for you and your applications.
It could be, ie. a storage for your application properties. You can
access your properties, using its name and your application id, both
defined by you. Each property within one application id has to be
unique. You can have as many properties and applications as you wish.
Maximum size of property name is 35 characters and maximum size of its
value is 10kB (10240 characters).
More detailed instruction: src/server/src/main/webapp/index.html.
Usage
Every operations are done via http://[your server]/ndss-server/ndss invoked with parameter:

1. *operation* set to one of:
- add - must be invoked with additional parameter: propertyName. Parameter propertyValue is
optional. It is possible to store empty entries. Returns true if operation done with success,
false otherwise.
- update - must be invoked with additional parameters: propertyName, propertyValue. If no
propertyValue parameter exists, the value will be nullified. Returns true if operation
done with success, false otherwise.
- get - must be invoked with additional parameter propertyName. Returns string
representation of the entry value or string 'null' in case of empty value.
- list - returns list of entries within the SGID given by sgid parameter, in form:
`[key]::==::[last modification date]::==::[value]`
delete - must be invoked with additional parameter propertyName. Returns true if operation done with success,
false otherwise.
2. *sgid* - value set to your application id, called Storage Group ID
3. *format* set to one of:
- text
- html
- xml
- json

**Examples**:

I. **Direct URL**
1. Add new entry
http://[yourserver]/ndss-server/ndss?operation=add&propertyName=newEntry1&format=[text, html, xml or json]&sgid=[your application id here]

2. Update entry
http://[your server]/ndss-server/ndss?operation=update&propertyName=new&format=[text, html, xml or json]&sgid=[your application id here]

3. Read entry
http://[your server]/ndss-server/ndss?operation=get&format=[text, html, xml or json]&propertyName=new&sgid=[your application id here]

4. List entries
http://[your server]/ndss-server/ndss?operation=list&format=[text, html, xml or json]&sgid=[your application id here]

5. Delete entry
http://[your server]/ndss-server/ndss?operation=delete&format=[text, html, xml or json]&propertyName=new&sgid=[your application id here]

II. **Java**
Download the class NDSClient. It is prepared to work with a configuration file called 'client.conf'. It is also
using log4j. If you do not want to logging, remove any line containing 'LOGGER' word from NDSClient.java
file and import of org.apache.log4j.Logger. You can simplify your life by using NDSClient, just like:

add new entry

`NDSClient client = new NDSClient(NDSClient.Operation.ADD, name, value, sgid, format);
System.out.println(client.call());`

update entry

`NDSClient client = new NDSClient(NDSClient.Operation.UPDATE, name, value, sgid, format);
System.out.println(client.call());`

read entry

`NDSClient client = new NDSClient(NDSClient.Operation.GET, name, sgid, format);
System.out.println(client.call());`

list entries

`NDSClient client = new NDSClient(NDSClient.Operation.LIST, null, sgid, format);
System.out.println(client.call());`

delete entry

`NDSClient client = new NDSClient(NDSClient.Operation.DELETE, name, sgid, format);
System.out.println(client.call());`


Changelog:

updated 16.12.2020
* Corrections in documentation.

updated 12.12.2020
* Fixed swapped values: name and last update date.
* Added information about entry creation date.

updated 19.11.2020
* Added XML and JSON as output format for the data.

updated 31.08.2016
* Added verification of data read from database, before creating resulting string.

updated 30.09.2013
* NetProperties renamed to NDSS

updated 18.09.2013
* Added enum NDSClient.Operation to use instead of String represenations of the commands. 

updated 14.09.2013
* Added "HTML output" checkbox to test form.

updated 12.09.2013
* Added test form

updated 06.09.2013
* Corrected index columns to avoid problem with adding new entry with the same name, but with another SGID.
* Corrected database connection config and IP address in this page, after provider's changes. 
