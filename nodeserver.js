var bytes = [];
var buffers = [];
var len = 0;

var cool = require('cool-ascii-faces');
var cloudinary = require('cloudinary');
var mkdirp = require('mkdirp');
var express = require('express');
var fs = require('fs');
var im = require('imagemagick');
var app = express();

var ImageFile = fs.createWriteStream("test.jpg");

const _dir = __dirname + "/images/";

cloudinary.config({
  cloud_name: 'mkm333',
  api_key: '912112275625765',
  api_secret: 'RYM6e5w_zf2s0KDrl0ozXFFTOus'
});

app.set('port', (process.env.PORT || 5000));

app.use(express.static(__dirname + '/public'));

// views is directory for all template files
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');

app.get('/', function(request, response) {
  response.render('pages/index');
});

app.get('/cool', function(request, response) {
  response.send(cool());
});

app.get('/download', function (req, res) {
  cloudinary.api.resources_by_tag("smallSize",function (result) {
            console.log(JSON.stringify(result));
            res.writeHead(200, {"Content-Type": "application/json"});
            res.write(JSON.stringify(result));
            res.end();
        })
});

app.post('/download', function (req, res) {

  cloudinary.api.resources_by_tag("smallSize",function (result) {
            console.log(JSON.stringify(result));
            res.writeHead(200, {"Content-Type": "application/json"});
            res.write(JSON.stringify(result));
            res.end();
        })

});

app.post('/upload', function (req, res) {
    savePhoto(req,res);
});


function savePhoto(req, res) {
        var date = new Date();
        var hours = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        var day = date.getDate();
        var mon = date.getMonth() + 1;
        var year = date.getFullYear();
        var d = day + "_" + mon + "_" + hours + "_" + year + "_" + min + "_" + sec + ".jpg"


        var timestamp = "33" + day + mon + hours + year + min + sec;
        fileName = _dir + d;
        fileName2 = _dir+timestamp+".jpg";
        console.log(fileName);
        console.log(fileName2);
        var ImageFile = fs.createWriteStream(fileName);
        var ImageFile2 = fs.createWriteStream(fileName2);
        ImageFile2.end();


        //on data
        req.on('data', function (data) {
            //write to file
            ImageFile.write(data);
        });

        req.on('end', function () {
            //finish write
            ImageFile.end();

            res.writeHead(200, "OK", { 'Content-Type': 'application/json' });
            res.write("Serwer otrzymał zdjęcie.");
            res.end();

            im.resize({
                      srcPath: fileName,
                      dstPath: fileName2,
                      height:   480
                    }, function(err, stdout, stderr){
                      if (err) throw err;
                      cloudinary.uploader.upload(fileName2, function (result) { console.log(result); },
                                  {
                                        public_id: timestamp,
                                        tags: ['smallSize']
                                  });

                      cloudinary.uploader.upload(fileName, function (result) { console.log(result); },
          		            {
          		                public_id: timestamp+"33",
          		                tags: ['fullSize']
          		            });
                  });

        });

    }

app.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});
