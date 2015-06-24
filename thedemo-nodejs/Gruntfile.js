module.exports = function(grunt) {
    require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);
    var exec = 'node_modules/jasmine-node/bin/jasmine-node  thedemotest_spec.js';
    grunt.initConfig({
        express: {
            default_option: {}
        },
        shell: {
            listFolders: {
                options: {
                    stdin: true,
                    stdout: true,
                    stderr: true,
                    failOnError: true
                },
                command: function () {
                    return exec;
                }
            }
        }
    });
    grunt.registerTask('test', ['shell']);
    grunt.registerTask('default', ['express', 'test']);
};