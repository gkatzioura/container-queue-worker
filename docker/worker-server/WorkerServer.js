const http = require('http');

const server = http.createServer((req, res) => {
    if (req.method === 'POST') {

       let body = '';
       req.on('data', chunk => {
            body += chunk.toString(); // convert Buffer to string
       });
       req.on('end', () => {
        console.log(body);
        res.end('ok');
       });
    }
    else {
      res.end(`
        <!doctype html>
        <html>
        <body>
            <h1>Worker server listening on 8080</h1>
        </body>
        </html>
      `);
    }
});
server.listen(8080);