#/bin/bash

if [ ! -d "versions" ]; then
  echo "Expects working directory to be project root"
  exit 1
fi

if [ -d "output_files" ]; then
  rm -rf output_files
fi

mkdir output_files

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <mod_version>"
fi

for file in $(find versions -name "stackupper-$1-*.jar")
do
  mv $file output_files
done
