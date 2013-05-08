wget https://dl.dropboxusercontent.com/u/51966419/NEMAZAT/Image.jar

if [ "$?" != "0" ]; then
    echo "Zkousim bez certifikatu"
    wget --no-check-certificate https://dl.dropboxusercontent.com/u/51966419/NEMAZAT/Image.jar
fi
