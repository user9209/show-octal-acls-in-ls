apt install openjdk-8-jre-headless

if [ ! -d ~/bin/ ]
then
  mkdir ~/bin/
fi
wget --no-cache -O ~/bin/lsOktalACL.class https://github.com/user9209/show-octal-acls-in-ls/raw/master/release/lsOktalACL.class

echo -e > ~/bin/lsOktalACL.sh "#!/bin/bash\nls -A -l -h --color=auto \$1 \$2 \$3 \$4 \$5 \$6 \$7 \$8 | java -cp ~/bin/ lsOktalACL"
chmod 755 ~/bin/lsOktalACL.sh

echo >> ~/.bashrc "alias ls='~/bin/lsOktalACL.sh'"
